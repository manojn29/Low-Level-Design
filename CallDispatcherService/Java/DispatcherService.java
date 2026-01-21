import java.util.UUID;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 1. The Agent (The Resource)
class Agent {
    private final String id;
    private final String name;

    public Agent(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    public String getName() { return name; }
    public String getId() { return id; }
    
    @Override
    public String toString() { return "Agent-" + name; }
}

// 2. The User Request (The Work Unit)
class CallRequest {
    private final String userId;
    private final boolean isCallback; // Priority flag

    public CallRequest(String userId, boolean isCallback) {
        this.userId = userId;
        this.isCallback = isCallback;
    }

    public String getUserId() { return userId; }
    public boolean isCallback() { return isCallback; }
}

class AgentPoolManager {
    // Queues for State Management
    private final Queue<Agent> availableAgents;
    private final Queue<CallRequest> callbackQueue; // Users waiting for a call back
    
    // Concurrency Controls
    private final ReentrantLock lock;
    
    public AgentPoolManager(int totalAgents) {
        this.availableAgents = new LinkedList<>();
        this.callbackQueue = new LinkedList<>();
        this.lock = new ReentrantLock(true); // "true" enables Fairness (prevents thread starvation)
        
        // Initialize the pool
        for (int i = 1; i <= totalAgents; i++) {
            availableAgents.add(new Agent("Staff_" + i));
        }
    }

    public Agent tryAcquireAgent() {
        lock.lock(); // Critical Section Starts
        try {
            if (availableAgents.isEmpty()) {
                return null;
            }
            return availableAgents.poll();
        } finally {
            lock.unlock(); // Critical Section Ends
        }
    }

    public void scheduleCallback(String userId) {
        lock.lock();
        try {
            System.out.println(">> No agents free. Scheduling callback for User: " + userId);
            callbackQueue.offer(new CallRequest(userId, true));
        } finally {
            lock.unlock();
        }
    }

    public void releaseAgent(Agent agent) {
        lock.lock();
        try {
            if (!callbackQueue.isEmpty()) {
                // Priority Logic: Assign this agent immediately to the waiting callback
                CallRequest waitingUser = callbackQueue.poll();
                triggerOutboundCall(agent, waitingUser);
            } else {
                // No callbacks waiting, return to pool
                availableAgents.offer(agent);
                System.out.println("Agent " + agent.getName() + " is back in the pool.");
            }
        } finally {
            lock.unlock();
        }
    }

    // Simulating the outbound dialer
    private void triggerOutboundCall(Agent agent, CallRequest request) {
        System.out.println("!! CALLBACK INITIATED: " + agent.getName() + " is calling User " + request.getUserId());
        // In a real app, this might spin off a new thread to handle the call duration
        new Thread(() -> simulateCallDuration(agent)).start();
    }
    
    // Helper to simulate work
    private void simulateCallDuration(Agent agent) {
        try {
            Thread.sleep(2000); // Talk for 2 seconds
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        releaseAgent(agent); // RECURSION: Agent finishes callback, becomes free again
    }
}

class CallDispatcher {
    private final AgentPoolManager agentManager;

    public CallDispatcher(AgentPoolManager manager) {
        this.agentManager = manager;
    }

    // This method is called by the REST Controller / WebSocket Handler
    public void handleIncomingCall(String userId) {
        Agent agent = agentManager.tryAcquireAgent();

        if (agent != null) {
            System.out.println("SUCCESS: User " + userId + " connected to " + agent.getName());
            
            // Simulate the call happening asynchronously
            new Thread(() -> {
                try {
                    Thread.sleep(3000); // Call lasts 3 seconds
                } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                
                System.out.println("Call Ended: " + agent.getName());
                agentManager.releaseAgent(agent);
            }).start();
            
        } else {
            // No agent available -> Schedule Callback
            agentManager.scheduleCallback(userId);
        }
    }
}

public class DispatcherService {
    public static void main(String[] args) {
        // 1. Initialize System with only 2 Agents
        AgentPoolManager pool = new AgentPoolManager(2);
        CallDispatcher dispatcher = new CallDispatcher(pool);

        ExecutorService requestSimulator = Executors.newFixedThreadPool(10);

        for (int i = 1; i <= 6; i++) {
            final String userId = "User-" + i;
            requestSimulator.submit(() -> {
                System.out.println("Incoming call from: " + userId);
                dispatcher.handleIncomingCall(userId);
            });
            
            // Small stagger to make logs readable
            try { Thread.sleep(200); } catch (Exception e) {}
        }
        
        requestSimulator.shutdown();
    }
}