import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import java.lang.IndexOutOfBoundsException;
import java.lang.NullPointerException;
import java.lang.IllegalArgumentException;

public class BaseballElimination {
    
    private int     numTeams;
    private int[][] teamRemainingGameCountMatrix;
    private int[]   teamWinCount;
    private int[]   teamLossCount;
    private int[]   teamRemainingGameCount;
    
    private ST<String, Integer> teamIndexST;
    private String[]            teamIndexArr;
    
    private boolean[] teamEliminated;
    private boolean[][] teamEliminationMinCutBitmap;
    
    private void ComputeMinCutBitmap(FordFulkerson fordFulkersonObj, int teamIdxToExclude) {
        int teamIdx1;
        int numGames = numTeams*numTeams;
        
        for (teamIdx1=0; teamIdx1<numTeams; teamIdx1++) {
            if (teamIdxToExclude == teamIdx1)
                continue;
            
            if (fordFulkersonObj.inCut(numGames + teamIdx1)) 
                teamEliminationMinCutBitmap[teamIdxToExclude][teamIdx1] = true;
        }
    }
    
    private void BuildFlowNetwork() {
        int teamIdxToExclude = 0;
        
        int numGames = numTeams * numTeams; // Including invalid similar pairs
        int sourceVertex = numGames+numTeams;
        int sinkVertex   = numGames+numTeams+1;
        while (teamIdxToExclude < numTeams) {
            FlowNetwork flowNetworkObj = new FlowNetwork(numGames + numTeams + 2);
            
            int teamIdx1 = 0; 
            int teamIdx2 = 0;
            
            int maxFlowToBeAchieved = 0;
            teamEliminated[teamIdxToExclude] = false;
            for (teamIdx1=0; teamIdx1<numTeams; teamIdx1++) {
                if (teamIdx1 == teamIdxToExclude)
                    continue;
                if (teamWinCount[teamIdxToExclude]+teamRemainingGameCount[teamIdxToExclude] < teamWinCount[teamIdx1]) {
                    teamEliminationMinCutBitmap[teamIdxToExclude][teamIdx1] = true;
                    teamEliminated[teamIdxToExclude] = true;
                    break;
                }
                flowNetworkObj.addEdge(new FlowEdge(numGames+teamIdx1, 
                                                    sinkVertex, 
                                                    (teamWinCount[teamIdxToExclude]+teamRemainingGameCount[teamIdxToExclude]-teamWinCount[teamIdx1])));
                for (teamIdx2=teamIdx1+1; teamIdx2<numTeams; teamIdx2++) {
                    if (teamIdx2 == teamIdxToExclude)
                        continue;
                    int gameVertex = teamIdx1*numTeams + teamIdx2; 
                    flowNetworkObj.addEdge(new FlowEdge(gameVertex, numGames+teamIdx1, Double.POSITIVE_INFINITY));
                    flowNetworkObj.addEdge(new FlowEdge(gameVertex, numGames+teamIdx2, Double.POSITIVE_INFINITY));
                    flowNetworkObj.addEdge(new FlowEdge(sourceVertex, gameVertex, teamRemainingGameCountMatrix[teamIdx1][teamIdx2]));
                    maxFlowToBeAchieved += teamRemainingGameCountMatrix[teamIdx1][teamIdx2];
                }
            }
            
            if (!teamEliminated[teamIdxToExclude]) {
                FordFulkerson fordFulkersonObj = new FordFulkerson(flowNetworkObj, sourceVertex, sinkVertex);
                if (fordFulkersonObj.value() < maxFlowToBeAchieved) {
                    teamEliminated[teamIdxToExclude] = true;
                    ComputeMinCutBitmap(fordFulkersonObj, teamIdxToExclude);
                    //StdOut.println("Team Eliminated");
                    //StdOut.println(teamIndexArr[teamIdxToExclude]);
                } else 
                    teamEliminated[teamIdxToExclude] = false;
            }
            teamIdxToExclude++;
        }
    }
    
    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In inputStreamObj            = new In(filename);
        numTeams                     = inputStreamObj.readInt();
        teamWinCount                 = new int[numTeams];
        teamLossCount                = new int[numTeams];
        teamRemainingGameCount       = new int[numTeams];
        teamRemainingGameCountMatrix = new int[numTeams][numTeams];
        
        teamIndexST                  = new ST<String, Integer>();
        teamIndexArr                 = new String[numTeams];
        
        teamEliminated               = new boolean[numTeams];
        teamEliminationMinCutBitmap  = new boolean[numTeams][numTeams];
        
        int teamNum = 0;
        while (!inputStreamObj.isEmpty()) {
            String teamName = inputStreamObj.readString();
            teamIndexArr[teamNum] = teamName; 
            teamIndexST.put(teamName, teamNum);
            teamWinCount[teamNum] = inputStreamObj.readInt();
            teamLossCount[teamNum] = inputStreamObj.readInt();
            teamRemainingGameCount[teamNum] = inputStreamObj.readInt();
            
            int otherTeamIdx = 0; 
            while (otherTeamIdx < numTeams) {
                teamRemainingGameCountMatrix[teamNum][otherTeamIdx] = inputStreamObj.readInt();  
                otherTeamIdx++;
            }
            teamNum++;
        }
        BuildFlowNetwork();
    }
    
    // number of teams
    public int numberOfTeams() {
        return numTeams;
    }
    
    // all teams
    public Iterable<String> teams() {
        Queue<String> teamsInDivision = new Queue<String>();
        
        for (String teamName : teamIndexST) 
            teamsInDivision.enqueue(teamName);
        
        return teamsInDivision;
    }
    
    public int wins(String team) {
        if (!teamIndexST.contains(team))
            throw new IllegalArgumentException("Invalid team specified");
        int teamIndex = teamIndexST.get(team);
        return teamWinCount[teamIndex];
    }
    
    // number of losses for given team
    public int losses(String team) {
        if (!teamIndexST.contains(team))
            throw new IllegalArgumentException("Invalid team specified");        
        int teamIndex = teamIndexST.get(team);
        return teamLossCount[teamIndex];
    }
    
    // number of remaining games for given team
    public int remaining(String team) {
        if (!teamIndexST.contains(team))
            throw new IllegalArgumentException("Invalid team specified");         
        int teamIndex = teamIndexST.get(team);
        return teamRemainingGameCount[teamIndex];
    }
    
    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!teamIndexST.contains(team1) || !teamIndexST.contains(team2))
            throw new IllegalArgumentException("Invalid team specified");         
        int team1Index = teamIndexST.get(team1);
        int team2Index = teamIndexST.get(team2);
        
        return teamRemainingGameCountMatrix[team1Index][team2Index];
    }
    
    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!teamIndexST.contains(team))
            throw new IllegalArgumentException("Invalid team specified");
        int teamIndex = teamIndexST.get(team);
        return teamEliminated[teamIndex];
    }
    
    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!teamIndexST.contains(team))
            throw new IllegalArgumentException("Invalid team specified");
        
        int teamIndex = teamIndexST.get(team);
        if (!teamEliminated[teamIndex])
            return null;

        Queue<String> queueObj = new Queue<String>();
        
        int teamIdx1;
        for (teamIdx1=0; teamIdx1<numTeams; teamIdx1++) {
            if (teamEliminationMinCutBitmap[teamIndex][teamIdx1])
                queueObj.enqueue(teamIndexArr[teamIdx1]);
        }
            
        return queueObj;
    }
    
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}