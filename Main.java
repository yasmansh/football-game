import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.out;

class League {
    private static ArrayList<Team> teams = new ArrayList<>();
    public static ArrayList<Player> allPlayers = new ArrayList<>();
    public static ArrayList<Player> freePlayers = new ArrayList<>();

    public static boolean hasTeam(String name) {
        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean allTeamsAreComplete() {
        for (int i = 0; i < teams.size(); i++) {
            if (!Team.isComplete(teams.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasPlayer(String firstName, String lastName, ArrayList<Player> players) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getLastName().equals(lastName) && players.get(i).getFirstName().equals(firstName)) {
                return true;
            }
        }
        return false;
    }

    public static void addNewTeam(Team team) {
        teams.add(team);
    }

    public static void addNewPlayer(Player player) {
        allPlayers.add(player);
        freePlayers.add(player);
    }

    public static void deleteTeam(String teamName) {
        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).getName().equals(teamName)) {
                for (int j = 0; j < teams.get(i).getSquad().size(); j++) {
                    Player.terminationOfAllContract(teams.get(i).getSquad().get(j));
                    freePlayers.add(teams.get(i).getSquad().get(j));
                    allPlayers.add(teams.get(i).getSquad().get(j));
                }
                for (int j = 0; j < teams.get(i).getRreservePlayers().size(); j++) {
                    Player.terminationOfAllContract(teams.get(i).getRreservePlayers().get(j));
                    freePlayers.add(teams.get(i).getRreservePlayers().get(j));
                    allPlayers.add(teams.get(i).getRreservePlayers().get(j));
                }
                teams.remove(i);
                return;
            }
        }
    }

    public static void bubbleSortForPlayers(ArrayList<Player> players) {
        for (int i = 0; i < players.size() - 1; i++) {
            for (int j = 0; j < players.size() - i - 1; j++) {
                if (players.get(j).getPositionNumber() > players.get(j + 1).getPositionNumber()) {
                    Collections.swap(players, j, j + 1);
                }
            }
        }

        for (int i = 0; i < players.size() - 1; i++) {
            for (int j = 0; j < players.size() - i - 1; j++) {

                if ((players.get(j).getPositionNumber() == players.get(j + 1).getPositionNumber() && players.get(j).getLastName()
                        .compareTo(players.get(j + 1).getLastName()) > 0) ||
                        (players.get(j).getPositionNumber() == players.get(j + 1).getPositionNumber() && players.get(j).getLastName().
                                equals(players.get(j + 1).getLastName()) && (players.get(j).getFirstName().compareTo(players.
                                get(j + 1).getFirstName()) > 0))) {
                    Collections.swap(players, j, j + 1);
                }
            }
        }

    }

    public static void PrintFreeAgents() {

        bubbleSortForPlayers(freePlayers);
        for (int i = 0; i < freePlayers.size(); i++) {
            out.println(i + 1 + ". " + freePlayers.get(i).getFirstName() + " " + freePlayers.get(i).
                    getLastName() + " " + freePlayers.get(i).getPosition());
        }
    }

    public static void PrintStatsOfTeam(String teamName) {
        out.println("Team: " + teamName + "\nBudget: " + findTeam(teamName).getBudget() + "\nSquad players:");
        bubbleSortForPlayers(findTeam(teamName).getSquad());
        printPlayers(findTeam(teamName).getSquad());
        out.println("Reserve players:");
        bubbleSortForPlayers(findTeam(teamName).getRreservePlayers());
        printPlayers(findTeam(teamName).getRreservePlayers());
    }

    private static void printPlayers(ArrayList<Player> players) {
        for (int j = 0; j < players.size(); j++) {
            out.println(j + 1 + ". " + players.get(j).getFirstName() + " " + players.get(j).
                    getLastName() + " " + players.get(j).getPosition() + " Age: " + players.get(j).getAge()
                    + " Contract: " + players.get(j).lengthOfTheCurrentContract + " years");
        }
    }

    public static void PrintStatsOfPlayer(String firstName, String lastName) {

        out.println(firstName + " " + lastName + " " + findPlayer(firstName, lastName).getPosition() + "\nAge: "
                + findPlayer(firstName, lastName).getAge());
        if (findPlayer(firstName, lastName).getPosition().equals("GK")) {
            out.println("Shot Saving: " + findPlayer(firstName, lastName).getPower1() + "\nReactions: " +
                    findPlayer(firstName, lastName).getPower2() + "\nPenalty Saving: " + findPlayer(firstName, lastName)
                    .getPower3());
        } else if (findPlayer(firstName, lastName).getPosition().equals("DF")) {
            out.println("Strength: " + findPlayer(firstName, lastName).getPower1() + "\nAggression: " +
                    findPlayer(firstName, lastName).getPower2() + "\nHeading: " + findPlayer(firstName, lastName).
                    getPower3());
        } else if (findPlayer(firstName, lastName).getPosition().equals("MF")) {
            out.println("Passing: " + findPlayer(firstName, lastName).getPower1() + "\nShooting: " +
                    findPlayer(firstName, lastName)
                            .getPower2() + "\nCrossing: " + findPlayer(firstName, lastName).getPower3());
        } else {
            out.println("Heading: " + findPlayer(firstName, lastName).getPower1() + "\nFinishing: " +
                    findPlayer(firstName, lastName)
                            .getPower2() + "\nPenalties: " + findPlayer(firstName, lastName).getPower3());
        }

    }

    public static void moveFreeAgent(String firstName, String lastName, String teamName, int contractLength) {
        for (int j = 0; j < freePlayers.size(); j++) {
            if (freePlayers.get(j).getLastName().equals(lastName) && freePlayers.get(j).getFirstName().equals(firstName)) {
                findTeam(teamName).addSavePlayer(freePlayers.get(j));
                freePlayers.get(j).mainTeam = findTeam(teamName);
                freePlayers.get(j).contract = true;
                freePlayers.get(j).setContractLength(contractLength);
                freePlayers.get(j).lengthOfTheCurrentContract = contractLength;
                freePlayers.remove(j);
                break;
            }
        }
    }

    public static Team findTeam(String teamName) {
        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).getName().equals(teamName)) {
                return teams.get(i);
            }
        }
        return teams.get(0);
    }

    public static Player findPlayer(String firstName, String lastName) {
        for (int i = 0; i < allPlayers.size(); i++) {
            if (allPlayers.get(i).getLastName().equals(lastName) && allPlayers.get(i).getFirstName().equals(firstName)) {
                return allPlayers.get(i);
            }
        }
        return allPlayers.get(0);
    }


    public static void termination(Player player) {

        if (!player.contract && !player.loanAgreement) {
            out.println("invalid termination command");
            return;
        }
        if (player.loanAgreement) {
            if (player.loanTeam.getSquad().contains(player)) {
                player.loanTeam.removeFromPosition(player, player.getPosition());
            }
            player.loanTeam.removePlayer(player);
            player.loanAgreement = false;
            player.setLoanAgreementLength(0);
            if (player.contract && player.getContractLength() > 0) {
                player.contract = true;
                player.mainTeam.addSavePlayer(player);
                player.lengthOfTheCurrentContract = player.getContractLength();
            } else {
                freePlayers.add(player);
                allPlayers.add(player);
            }
        } else {
            if (player.mainTeam.getSquad().contains(player)) {
                player.mainTeam.removeFromPosition(player, player.getPosition());
            }
            player.mainTeam.removePlayer(player);
            player.contract = false;
            player.setContractLength(0);
            freePlayers.add(player);
            allPlayers.add(player);
        }

    }

    public static void sell(Player player, Team sourceTeam, Team destinationTeam, int price, int contractLenght) {
        if (destinationTeam.getBudget() < price) {
            out.println("insufficient budget");
            return;
        }
        sourceTeam.increaseBudget(price);
        destinationTeam.decreaseBudget(price);
        if (sourceTeam.getSquad().contains(player)) {
            sourceTeam.removeFromPosition(player, player.getPosition());
        }
        sourceTeam.removePlayer(player);
        destinationTeam.addSavePlayer(player);
        player.lengthOfTheCurrentContract = contractLenght;
        if (!player.loanAgreement) {
            player.contract = true;
            player.setContractLength(contractLenght);
            player.mainTeam = destinationTeam;
        } else {
            player.setLoanAgreementLength(contractLenght);
            player.loanTeam = destinationTeam;
        }
    }

    public static void loan(Player player, Team sourceTeam, Team destinationTeam, int loanAgreementLength) {
        if (player.getContractLength() < loanAgreementLength) {
            out.println("invalid loan contract");
            return;
        }
        player.loanAgreement = true;
        player.setLoanAgreementLength(loanAgreementLength);
        player.lengthOfTheCurrentContract = loanAgreementLength;
        player.loanTeam = destinationTeam;
        destinationTeam.addSavePlayer(player);
        if (sourceTeam.getSquad().contains(player)) {
            sourceTeam.removeFromPosition(player, player.getPosition());
        }
        sourceTeam.removePlayer(player);
    }

    public static void printIncompleteTeams() {
        ArrayList<Team> incompleteTeams = new ArrayList<>();
        for (int i = 0; i < teams.size(); i++) {
            if (!Team.isComplete(teams.get(i))) {
                incompleteTeams.add(teams.get(i));
            }
        }

        for (int i = 0; i < incompleteTeams.size() - 1; i++) {
            for (int j = 0; j < incompleteTeams.size() - i - 1; j++) {

                if (incompleteTeams.get(j).getName().compareTo(incompleteTeams.get(j + 1).getName()) > 0) {
                    Collections.swap(incompleteTeams, j, j + 1);
                }
            }
        }
        for (int i = 0; i < incompleteTeams.size(); i++) {
            out.println(incompleteTeams.get(i).getName() + " squad isn't complete");
        }
    }

    public static void friendlyMatch(Team team1, Team team2) {
        int team1Goals = team1.getFriendlyMatchGoals();
        int team2Goals = team2.getFriendlyMatchGoals();
        match(team1, team2, 0);
        team1Goals = team1.getFriendlyMatchGoals() - team1Goals;
        team2Goals = team2.getFriendlyMatchGoals() - team2Goals;
        out.println(team1.getName() + " " + team1Goals + " - " + team2Goals + " " + team2.getName());
    }

    private static void match(Team team1, Team team2, int guest) {
        ArrayList<Player> GK1 = new ArrayList<>();
        ArrayList<Player> GK2 = new ArrayList<>();
        ArrayList<Player> DF1 = new ArrayList<>();
        ArrayList<Player> DF2 = new ArrayList<>();
        ArrayList<Player> MF1 = new ArrayList<>();
        ArrayList<Player> MF2 = new ArrayList<>();
        ArrayList<Player> ST1 = new ArrayList<>();
        ArrayList<Player> ST2 = new ArrayList<>();
        int team1Goals = 0;
        int team2Goals = 0;
        copy(team1, GK1, DF1, MF1, ST1);
        copy(team2, GK2, DF2, MF2, ST2);
        if (guest == 1) {
            decreasePowerAll(GK1, DF1, MF1, ST1);
        } else if (guest == 2) {
            decreasePowerAll(GK2, DF2, MF2, ST2);
        }
        MidfieldPasses(MF1, MF2, ST1, ST2);
        DefenderStrength(DF1, DF2, ST1, ST2);
        for (int i = 0; i < 3; i++) {
            if (ST1.get(i).getPower2() > GK2.get(0).getPower2())
                team1Goals++;
            if (ST2.get(i).getPower2() > GK1.get(0).getPower2())
                team2Goals++;
        }
        if (getAverageFinishing(ST1) - 5 >= GK2.get(0).getPower2())
            team1Goals += 2;
        if (getAverageFinishing(ST2) - 5 >= GK1.get(0).getPower2())
            team2Goals += 2;
        for (int i = 0; i < 3; i++) {
            if (MF1.get(i).getPower2() > GK2.get(0).getPower1())
                team1Goals++;
            if (MF2.get(i).getPower2() > GK1.get(0).getPower1())
                team2Goals++;
            if (ST1.get(i).getPower1() * getAverageCrossing(MF1) > GK2.get(0).getPower2() * GK2.get(0).getPower2())
                team1Goals++;
            if (ST2.get(i).getPower1() * getAverageCrossing(MF2) > GK1.get(0).getPower2() * GK1.get(0).getPower2())
                team2Goals++;
        }
        for (int i = 0; i < 4; i++) {
            if (DF1.get(i).getPower3() * getAverageCrossing(MF1) > GK2.get(0).getPower2() * GK2.get(0).getPower2()) {
                team1Goals++;
            }
            if (DF2.get(i).getPower3() * getAverageCrossing(MF2) > GK1.get(0).getPower2() * GK1.get(0).getPower2()) {
                team2Goals++;
            }
        }
        team1Goals = penaltyGoals(GK2, DF2, ST1, team1Goals);
        team2Goals = penaltyGoals(GK1, DF1, ST2, team2Goals);
        if (setTheResaultOfTheFriendlyMatch(team1, team2, guest, team1Goals, team2Goals)) return;
        setTheResultOfTheMatch(team1, team2, team1Goals, team2Goals);
    }

    private static void DefenderStrength(ArrayList<Player> DF1, ArrayList<Player> DF2,
                                         ArrayList<Player> ST1, ArrayList<Player> ST2) {
        if (getAverageStrength(DF1) > 85) {
            for (int i = 0; i < 3; i++) {
                ST2.get(i).decreasePower2(3);
            }
        }
        if (getAverageStrength(DF2) > 85) {
            for (int i = 0; i < 3; i++) {
                ST1.get(i).decreasePower2(3);
            }
        }
        if (hasPowerfulDF(DF1)) {
            for (int i = 0; i < 3; i++) {
                ST2.get(i).decreasePower2(3);
            }
        }
        if (hasPowerfulDF(DF2)) {
            for (int i = 0; i < 3; i++) {
                ST1.get(i).decreasePower2(3);
            }
        }
    }

    private static void MidfieldPasses(ArrayList<Player> MF1, ArrayList<Player> MF2,
                                       ArrayList<Player> ST1, ArrayList<Player> ST2) {
        if (getAveragePassing(MF1) > 85) {
            for (int i = 0; i < 3; i++) {
                ST1.get(i).increasePower2(5);
            }
        }
        if (hasPowerfulMF(MF1)) {
            for (int i = 0; i < 3; i++) {
                ST1.get(i).increasePower2(5);
            }
        }
        if (getAveragePassing(MF2) > 85) {
            for (int i = 0; i < 3; i++) {
                ST2.get(i).increasePower2(5);
            }
        }
        if (hasPowerfulMF(MF2)) {
            for (int i = 0; i < 3; i++) {
                ST2.get(i).increasePower2(5);
            }
        }
    }

    private static int penaltyGoals(ArrayList<Player> GK2, ArrayList<Player> DF2, ArrayList<Player> ST1, int team1Goals) {
        if (getAverageAggression(DF2) > 85) {
            int maxPenalties = ST1.get(0).getPower3();
            for (int i = 1; i < 3; i++) {
                if (ST1.get(i).getPower3() > maxPenalties) {
                    maxPenalties = ST1.get(i).getPower3();
                }
            }
            if (maxPenalties > GK2.get(0).getPower3()) {
                team1Goals++;
            }
        }
        return team1Goals;
    }

    private static boolean setTheResaultOfTheFriendlyMatch(Team team1, Team team2,
                                                           int guest, int team1Goals, int team2Goals) {
        if (guest == 0) { //Friendly Match
            team1.friendlyMatchGoals += team1Goals;
            team2.friendlyMatchGoals += team2Goals;
            return true;
        }
        return false;
    }

    private static void setTheResultOfTheMatch(Team team1, Team team2, int team1Goals, int team2Goals) {
        if (team1Goals > team2Goals) {
            team1.goalDifference += team1Goals - team2Goals;
            team2.goalDifference += team2Goals - team1Goals;
            team1.numberOfWins++;
            team2.numberOfLosts++;
            team1.score += 3;
            team1.numberOfGoals += team1Goals;
            team1.numerOfGoalLosted += team2Goals;
            team2.numberOfGoals += team2Goals;
            team2.numerOfGoalLosted += team1Goals;
        } else if (team2Goals > team1Goals) {
            team2.goalDifference += team2Goals - team1Goals;
            team1.goalDifference += team1Goals - team2Goals;
            team1.numberOfLosts++;
            team2.numberOfWins++;
            team2.score += 3;
            team2.numerOfGoalLosted += (team1Goals);
            team2.numberOfGoals += (team2Goals);
            team1.numberOfGoals += (team1Goals);
            team1.numerOfGoalLosted += (team2Goals);
        } else {
            team1.numberOfEquals++;
            team2.numberOfEquals++;
            team1.score++;
            team2.score++;
            team2.numberOfGoals += (team2Goals);
            team2.numerOfGoalLosted += (team1Goals);
            team1.numberOfGoals += (team1Goals);
            team1.numerOfGoalLosted += (team2Goals);
        }
    }

    public static double getAverageAggression(ArrayList<Player> DF) {
        double sum = 0;
        for (int i = 0; i < 4; i++) {
            sum += DF.get(i).getPower2();
        }
        return sum / 4;
    }

    public static double getAverageCrossing(ArrayList<Player> MF) {
        double sum = 0;
        for (int i = 0; i < 3; i++) {
            sum += MF.get(i).getPower3();
        }
        return sum / 3;
    }

    public static double getAverageFinishing(ArrayList<Player> ST) {
        double sum = 0;
        for (int i = 0; i < 3; i++) {
            sum += ST.get(i).getPower2();
        }
        return sum / 3;
    }

    public static double getAveragePassing(ArrayList<Player> MF) {
        double sum = 0;
        for (int i = 0; i < 3; i++) {
            sum += MF.get(i).getPower1();
        }
        return sum / 3;
    }

    public static double getAverageStrength(ArrayList<Player> DF) {
        double sum = 0;
        for (int i = 0; i < 4; i++) {
            sum += DF.get(i).getPower1();
        }
        return sum / 4;
    }

    public static boolean hasPowerfulMF(ArrayList<Player> MF) {
        for (int i = 0; i < 3; i++) {
            if (MF.get(i).getPower1() > 90) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasPowerfulDF(ArrayList<Player> DF) {
        for (int i = 0; i < 4; i++) {
            if (DF.get(i).getPower1() > 90) {
                return true;
            }
        }
        return false;
    }

    private static void decreasePowerAll(ArrayList<Player> GK, ArrayList<Player> DF,
                                         ArrayList<Player> MF, ArrayList<Player> ST) {
        GK.get(0).decreasePower1(5);
        GK.get(0).decreasePower2(5);
        GK.get(0).decreasePower3(5);
        for (int i = 0; i < 4; i++) {
            DF.get(i).decreasePower1(5);
            DF.get(i).decreasePower2(5);
            DF.get(i).decreasePower3(5);
        }
        for (int i = 0; i < 3; i++) {
            MF.get(i).decreasePower1(5);
            MF.get(i).decreasePower2(5);
            MF.get(i).decreasePower3(5);
        }
        for (int i = 0; i < 3; i++) {
            ST.get(i).decreasePower1(5);
            ST.get(i).decreasePower2(5);
            ST.get(i).decreasePower3(5);
        }
    }

    private static void copy(Team team, ArrayList<Player> GK, ArrayList<Player> DF,
                             ArrayList<Player> MF, ArrayList<Player> ST) {
        for (int i = 0; i < 11; i++) {
            switch (team.getSquad().get(i).getPosition()) {
                case "GK": {
                    Player newPlayer = new Player(team.getSquad().get(i).getPower1(), team.getSquad().get(i).getPower2(),
                            team.getSquad().get(i).getPower3());
                    GK.add(newPlayer);
                    break;
                }
                case "DF": {
                    Player newPlayer = new Player(team.getSquad().get(i).getPower1(), team.getSquad().get(i).getPower2(),
                            team.getSquad().get(i).getPower3());
                    DF.add(newPlayer);
                    break;
                }
                case "MF": {
                    Player newPlayer = new Player(team.getSquad().get(i).getPower1(), team.getSquad().get(i).getPower2(),
                            team.getSquad().get(i).getPower3());
                    MF.add(newPlayer);
                    break;
                }
                case "ST": {
                    Player newPlayer = new Player(team.getSquad().get(i).getPower1(), team.getSquad().get(i).getPower2(),
                            team.getSquad().get(i).getPower3());
                    ST.add(newPlayer);
                    break;
                }
            }
        }
    }

    public static void nextSeason() {
        if (!League.allTeamsAreComplete()) {
            League.printIncompleteTeams();
            return;
        }

        for (int i = 0; i < teams.size() - 1; i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                match(teams.get(i), teams.get(j), 1);
                match(teams.get(i), teams.get(j), 2);
            }
        }
        int first = 0;
        int second = 0;
        int third = 0;
        for (Team team : teams) {
            if (team.getScore() >= first) {
                third = second;
                second = first;
                first = team.getScore();
            } else if (team.getScore() >= second) {
                third = second;
                second = team.getScore();
            } else if (team.getScore() >= third) {
                third = team.getScore();
            }
        }
        for (Team team : teams) {
            if (team.getScore() == first) {
                team.increaseBudget(100);
            } else if (team.getScore() == second) {
                team.increaseBudget(50);
            } else if (team.getScore() == third) {
                team.increaseBudget(20);
            }
        }
        checkAllPlayers();
        printLeagueTable();
    }

    private static void checkAllPlayers() {
        for (int i = allPlayers.size() - 1; i >= 0; i--) {
            allPlayers.get(i).increaseAge();
            allPlayers.get(i).lengthOfTheCurrentContract--;
            allPlayers.get(i).decreaseContractLength();
            allPlayers.get(i).decreaseLoanAgreementLength();
            if (allPlayers.get(i).loanAgreement && allPlayers.get(i).getLoanAgreementLength() == 0) {
                allPlayers.get(i).loanAgreement = false;
                if (allPlayers.get(i).loanTeam.getSquad().contains(allPlayers.get(i))) {
                    allPlayers.get(i).loanTeam.removeFromPosition(allPlayers.get(i), allPlayers.get(i).getPosition());
                }
                allPlayers.get(i).loanTeam.removePlayer(allPlayers.get(i));
                if (allPlayers.get(i).getContractLength() > 0) {
                    allPlayers.get(i).mainTeam.addSavePlayer(allPlayers.get(i));
                    allPlayers.get(i).lengthOfTheCurrentContract = allPlayers.get(i).getContractLength();
                } else {
                    terminateMainContract(i);
                }
            }
            if (allPlayers.get(i).contract && allPlayers.get(i).getContractLength() == 0) {
                terminateMainContract(i);
            }
            checkAge(i);
        }
    }

    private static void checkAge(int i) {
        if (allPlayers.get(i).getAge() >= 40) {
            for (int j = 0; j < teams.size(); j++) {
                if (teams.get(j).getSquad().contains(allPlayers.get(i)) || teams.get(j).getRreservePlayers().
                        contains(allPlayers.get(i))) {
                    if (teams.get(j).getSquad().contains(allPlayers.get(i))) {
                        teams.get(j).removeFromPosition(allPlayers.get(i), allPlayers.get(i).getPosition());
                    }
                    teams.get(j).removePlayer(allPlayers.get(i));
                    break;
                }
            }
            if (freePlayers.contains(allPlayers.get(i))) {
                for (int t = 0; t < freePlayers.size(); t++) {
                    if (freePlayers.get(t).equals(allPlayers.get(i))) {
                        freePlayers.remove(t);
                        break;
                    }
                }
            }
            allPlayers.remove(i);
        }
    }

    private static void terminateMainContract(int i) {
        allPlayers.get(i).contract = false;
        if (allPlayers.get(i).mainTeam.getSquad().contains(allPlayers.get(i))) {
            allPlayers.get(i).mainTeam.removeFromPosition(allPlayers.get(i), allPlayers.get(i).getPosition());
        }
        allPlayers.get(i).mainTeam.removePlayer(allPlayers.get(i));
        freePlayers.add(allPlayers.get(i));
    }

    private static void printLeagueTable() {
        for (int i = 0; i < teams.size() - 1; i++) {
            for (int j = 0; j < teams.size() - i - 1; j++) {
                if (teams.get(j + 1).score > teams.get(j).score ||
                        (teams.get(j + 1).score == teams.get(j).score && teams.get(j + 1).goalDifference > teams.
                                get(j).goalDifference) || (teams.get(j + 1).score == teams.get(j).score && teams.
                        get(j + 1).goalDifference == teams.get(j).goalDifference && teams.get(j).getName().compareTo
                        (teams.get(j + 1).getName()) > 0)) {
                    Collections.swap(teams, j, j + 1);
                }
            }
        }
        for (int i = 0; i < teams.size(); i++) {
            out.println(i + 1 + ". " + teams.get(i).getName() + " " + teams.get(i).score + " points W: " +
                    teams.get(i).numberOfWins + " D: " + teams.get(i).numberOfEquals + " L: " + teams.get(i).
                    getNumberOfLosts() + " GF: " + teams.get(i).numberOfGoals + " GA: " + teams.get(i).
                    numerOfGoalLosted + " GD: " + printDF(teams.get(i).goalDifference));
        }
    }

    private static String printDF(int x) {
        if (x > 0) {
            return "+" + x;
        }
        return "" + x;
    }
}


class Team {
    private String name;
    private int budget;
    private ArrayList<Player> squad = new ArrayList<>();
    private ArrayList<Player> reservePlayers = new ArrayList<>();
    private ArrayList<Player> GK = new ArrayList<>();
    private ArrayList<Player> DF = new ArrayList<>();
    private ArrayList<Player> MF = new ArrayList<>();
    private ArrayList<Player> ST = new ArrayList<>();
    public int score;
    public int numberOfWins;
    public int numberOfEquals;
    public int numberOfLosts;
    public int numberOfGoals;
    public int numerOfGoalLosted;
    public int goalDifference;
    public int friendlyMatchGoals;

    Team(String name) {
        setName(name);
        budget = 100;
    }

    public static boolean isComplete(Team team) {
        return (team.GK.size() == 1 && team.DF.size() == 4 && team.MF.size() == 3 && team.ST.size() == 3);
    }

    public int indexOfPlayerInSquad(Player player) {
        for (int i = 0; i < squad.size(); i++) {
            if (squad.get(i).equals(player)) {
                return i;
            }
        }
        return 0;
    }

    public void put(Player player) {
        for (int i = 0; i < reservePlayers.size(); i++) {
            if (reservePlayers.get(i).equals(player)) {
                reservePlayers.remove(i);
                break;
            }
        }
        if (player.getPosition().equals("GK")) {
            if (GK.size() == 1) {
                reservePlayers.add(squad.get(indexOfPlayerInSquad(GK.get(0))));
                squad.remove(indexOfPlayerInSquad(GK.get(0)));
                GK.remove(0);
                GK.add(player);
                squad.add(player);
                return;
            }
            squad.add(player);
            GK.add(player);
        } else if (player.getPosition().equals("DF")) {
            if (DF.size() == 4) {
                reservePlayers.add(squad.get(indexOfPlayerInSquad(DF.get(0))));
                squad.remove(indexOfPlayerInSquad(DF.get(0)));
                DF.remove(0);
                DF.add(player);
                squad.add(player);
                return;
            }
            squad.add(player);
            DF.add(player);
        } else if (player.getPosition().equals("MF")) {
            if (MF.size() == 3) {
                reservePlayers.add(squad.get(indexOfPlayerInSquad(MF.get(0))));
                squad.remove(indexOfPlayerInSquad(MF.get(0)));
                MF.remove(0);
                MF.add(player);
                squad.add(player);
                return;
            }
            squad.add(player);
            MF.add(player);
        } else { //ST
            if (ST.size() == 3) {
                reservePlayers.add(squad.get(indexOfPlayerInSquad(ST.get(0))));
                squad.remove(indexOfPlayerInSquad(ST.get(0)));
                ST.remove(0);
                ST.add(player);
                squad.add(player);
                return;
            }
            squad.add(player);
            ST.add(player);
        }
    }

    public void increaseBudget(int price) {
        this.budget += price;
    }

    public void decreaseBudget(int price) {
        this.budget -= price;
    }

    public static boolean hasPlayerInTeam(Player player, Team team) {
        return team.getSquad().contains(player) || team.getRreservePlayers().contains(player);
    }

    public void addSavePlayer(Player player) {
        reservePlayers.add(player);
    }

    public ArrayList<Player> getSquad() {
        return squad;
    }

    public ArrayList<Player> getRreservePlayers() {
        return reservePlayers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getBudget() {
        return budget;
    }

    public void removePlayer(Player player) {
        if (squad.contains(player)) {
            for (int i = 0; i < squad.size(); i++) {
                if (squad.get(i).equals(player)) {
                    squad.remove(i);
                    return;
                }
            }
        } else {
            for (int i = 0; i < reservePlayers.size(); i++) {
                if (reservePlayers.get(i).equals(player)) {
                    reservePlayers.remove(i);
                    return;
                }
            }
        }
    }

    public int getNumberOfLosts() {
        return numberOfLosts;
    }

    public int getScore() {
        return score;
    }

    public void removeFromPosition(Player player, String position) {
        if (position.equals("GK") && GK.get(0).equals(player)) {
            GK.remove(0);
        } else if (position.equals("DF")) {
            for (int i = 0; i < DF.size(); i++) {
                if (DF.get(i).equals(player)) {
                    DF.remove(i);
                    return;
                }
            }
        } else if (position.equals("MF")) {
            for (int i = 0; i < MF.size(); i++) {
                if (MF.get(i).equals(player)) {
                    MF.remove(i);
                    return;
                }
            }
        } else if (position.equals("ST")) {
            for (int i = 0; i < ST.size(); i++) {
                if (ST.get(i).equals(player)) {
                    ST.remove(i);
                    return;
                }
            }
        }
    }

    public int getFriendlyMatchGoals() {
        return friendlyMatchGoals;
    }
}

class Player {
    private String firstName;
    private String lastName;
    private String position;
    private int positionNumber;
    private int age;
    boolean contract;
    boolean loanAgreement;
    private int contractLength;
    private int loanAgreementLength;
    public int lengthOfTheCurrentContract;
    private int power1;
    private int power2;
    private int power3;
    public Team mainTeam;
    public Team loanTeam;

    Player(String firstName, String lastName, int age, String position) {
        setFirstName(firstName);
        setLastName(lastName);
        setAge(age);
        setPosition(position);
    }

    Player(int power1, int power2, int power3) {
        setPower1(power1);
        setPower2(power2);
        setPower3(power3);
    }

    public void increaseAge() {
        age++;
    }

    public void decreaseContractLength() {
        contractLength--;
    }

    public void decreaseLoanAgreementLength() {
        loanAgreementLength--;
    }

    public void decreasePower1(int n) {
        power1 -= n;
    }

    public void increasePower2(int n) {
        power2 += n;
    }

    public void decreasePower2(int n) {
        power2 -= n;
    }

    public void decreasePower3(int n) {
        power3 -= n;
    }

    public void renew(int n) {
        contractLength += n;
        if (!loanAgreement) {
            lengthOfTheCurrentContract = contractLength;
        }
    }

    public static void terminationOfAllContract(Player player) {
        player.contract = false;
        player.loanAgreement = false;
        player.loanAgreementLength = 0;
        player.contractLength = 0;
        player.lengthOfTheCurrentContract = 0;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
        switch (position) {
            case "GK":
                positionNumber = 0;
                break;
            case "DF":
                positionNumber = 1;
                break;
            case "MF":
                positionNumber = 2;
                break;
            case "ST":
                positionNumber = 3;
                break;
        }
    }

    public int getPower1() {
        return power1;
    }

    public void setPower1(int power1) {
        this.power1 = power1;
    }

    public int getPower2() {
        return power2;
    }

    public void setPower2(int power2) {
        this.power2 = power2;
    }

    public int getPower3() {
        return power3;
    }

    public void setPower3(int power3) {
        this.power3 = power3;
    }

    public int getLoanAgreementLength() {
        return loanAgreementLength;
    }

    public void setLoanAgreementLength(int loanAgreementLength) {
        this.loanAgreementLength = loanAgreementLength;
    }

    public int getContractLength() {
        return contractLength;
    }

    public void setContractLength(int contractLength) {
        this.contractLength = contractLength;
    }

    public int getPositionNumber() {
        return positionNumber;
    }
}


public class Main {

    private static void menu(Scanner scanner) {
        String input = scanner.nextLine().trim();
        String[] partsOfInput = input.split("\\s+");
        if (partsOfInput[0].equals("create")) {
            create(scanner, input, partsOfInput);
        } else if (partsOfInput[0].equals("delete")) {
            delete(input, partsOfInput);
        } else if (partsOfInput[0].equals("print")) {
            print(input, partsOfInput);
        } else if (partsOfInput[0].equals("move")) {
            move(input, partsOfInput);
        } else if (partsOfInput[0].equals("renew")) {
            renew(input, partsOfInput);
        } else if (partsOfInput[0].equals("terminate")) {
            terminate(input, partsOfInput);
        } else if (partsOfInput[0].equals("sell") || partsOfInput[0].equals("loan")) {
            checkSellOrLoan(input, partsOfInput);
        } else if (partsOfInput[0].equals("put")) {
            put(input, partsOfInput);
        } else if (partsOfInput[0].equals("friendly")) {
            friendlyMatch(input, partsOfInput);
        } else if (input.equals("next season")) {
            League.nextSeason();
        } else if (input.equals("end")) {
            return;
        } else {
            printInvalidCommand();
        }
        menu(scanner);
    }

    private static void friendlyMatch(String input, String[] partsOfInput) {
        Matcher matcher = Pattern.compile("friendly\\s+match\\s+between\\s+(\\w+)\\s+and\\s+(\\w+)").matcher(input);
        if (matcher.find()) {
            if (!League.hasTeam(partsOfInput[3]) || !League.hasTeam(partsOfInput[5])) {
                printInvalidTeam();
            } else if (!Team.isComplete(League.findTeam(partsOfInput[3])) &&
                    !Team.isComplete(League.findTeam(partsOfInput[5]))) {
                printSquadIsNotComplete(partsOfInput[3]);
                printSquadIsNotComplete(partsOfInput[5]);
            } else if (!Team.isComplete(League.findTeam(partsOfInput[3]))) {
                printSquadIsNotComplete(partsOfInput[3]);
            } else if (!Team.isComplete(League.findTeam(partsOfInput[5]))) {
                printSquadIsNotComplete(partsOfInput[5]);
            } else {
                League.friendlyMatch(League.findTeam(partsOfInput[3]), League.findTeam(partsOfInput[5]));
            }
        } else {
            printInvalidCommand();
        }
    }

    private static void printSquadIsNotComplete(String teamName) {
        out.println(teamName + " squad isn't complete");
    }

    private static void put(String input, String[] partsOfInput) {

        Matcher matcher = Pattern.compile("put\\s+player\\s+(\\w+)\\s+(\\w+)\\s+from\\s+(\\w+)\\s+in\\s+main\\s+squad").
                matcher(input);
        if (matcher.find()) {
            if (!League.hasTeam(partsOfInput[5])) {
                printInvalidTeam();
            } else if (League.findTeam(partsOfInput[5]).getSquad().contains(League.findPlayer(partsOfInput[2],
                    partsOfInput[3]))) {
                out.println("player is currently in the main squad");
            } else if (!League.findTeam(partsOfInput[5]).getRreservePlayers().contains(League.findPlayer
                    (partsOfInput[2], partsOfInput[3]))) {
                printInvalidPlayer();
            } else {
                League.findTeam(partsOfInput[5]).put(League.findPlayer(partsOfInput[2], partsOfInput[3]));
            }
        } else {
            printInvalidCommand();
        }
    }

    private static void checkSellOrLoan(String input, String[] partsOfInput) {
        Matcher matcherOfSell = Pattern.compile("sell\\s+player\\s+(\\w+)\\s+(\\w+)\\s+from\\s+(\\w+)\\s+to\\s+(\\w+)" +
                "\\s+for\\s+(\\d+)\\s*\\$\\s+with\\s+(\\d+)\\s+years\\s+contract").matcher(input);
        Matcher matcherOfLoan = Pattern.compile("loan\\s+player\\s+(\\w+)\\s+(\\w+)\\s+from\\s+(\\w+)\\s+to\\s+(\\w+)" +
                "\\s+with\\s+(\\d+)\\s+years\\s+contract").matcher(input);

        if (matcherOfSell.find() || matcherOfLoan.find()) {
            if (!League.hasTeam(partsOfInput[5])) {
                out.println("invalid source team");
            } else if ((!League.hasPlayer(partsOfInput[2], partsOfInput[3], League.allPlayers)) || (!Team.hasPlayerInTeam
                    (League.findPlayer(partsOfInput[2], partsOfInput[3]), League.findTeam(partsOfInput[5])))) {
                printInvalidPlayer();
            } else if (!League.hasTeam(partsOfInput[7])) {
                out.println("invalid destination team");
            } else {
                if (partsOfInput[0].equals("sell")) {
                    League.sell(League.findPlayer(partsOfInput[2], partsOfInput[3]), League.findTeam(partsOfInput[5]),
                            League.findTeam(partsOfInput[7]), toInt(matcherOfSell.group(5)), toInt(matcherOfSell.group(6)));
                }
                if (partsOfInput[0].equals("loan")) {
                    League.loan(League.findPlayer(partsOfInput[2], partsOfInput[3]), League.findTeam(partsOfInput[5]),
                            League.findTeam(partsOfInput[7]), toInt(partsOfInput[9]));
                }
            }
        } else {
            printInvalidCommand();
        }
    }

    private static void terminate(String input, String[] partsOfInput) {
        Matcher matcher = Pattern.compile("terminate\\s+contract\\s+of\\s+(\\w+)\\s+(\\w+)").matcher(input);
        if (matcher.find()) {
            if (!League.hasPlayer(partsOfInput[3], partsOfInput[4], League.allPlayers)) {
                printInvalidPlayer();
            } else {
                League.termination(League.findPlayer(partsOfInput[3], partsOfInput[4]));
            }
        } else {
            printInvalidCommand();
        }
    }

    private static void renew(String input, String[] partsOfInput) {
        Matcher matcher = Pattern.compile("renew\\s+contract\\s+of\\s+(\\w+)\\s+(\\w+)\\s+for\\s+(\\d+)\\s+years").
                matcher(input);
        if (matcher.find()) {
            if (!League.hasPlayer(partsOfInput[3], partsOfInput[4], League.allPlayers)) {
                printInvalidPlayer();
            } else if (!League.findPlayer(partsOfInput[3], partsOfInput[4]).loanAgreement &&
                    !League.findPlayer(partsOfInput[3], partsOfInput[4]).contract) {
                out.println("invalid renewal command");
            } else {
                League.findPlayer(partsOfInput[3], partsOfInput[4]).renew(toInt(partsOfInput[6]));
                out.println("contract renewed. new contract is valid for " + League.findPlayer(partsOfInput[3],
                        partsOfInput[4]).getContractLength() + " years");
            }
        } else {
            printInvalidCommand();
        }
    }

    private static void printInvalidPlayer() {
        out.println("invalid player");
    }

    private static void move(String input, String[] partsOfInput) {
        Matcher matcher = Pattern.compile("(move\\s+free\\s+agent\\s+)(\\w+)\\s+(\\w+)\\s+to\\s+(\\w+)\\s+with\\s+" +
                "(\\d+)\\s+years\\s+contract").matcher(input);
        if (matcher.find()) {
            if (!League.hasTeam(partsOfInput[6])) {
                printInvalidTeam();
            } else if (!League.hasPlayer(partsOfInput[3], partsOfInput[4], League.freePlayers)) {
                out.println("invalid free agent");
            } else {
                League.moveFreeAgent(partsOfInput[3], partsOfInput[4], partsOfInput[6], toInt(partsOfInput[8]));
                out.println("free agent moved");
            }
        } else {
            printInvalidCommand();
        }
    }

    private static void printInvalidTeam() {
        out.println("invalid team");
    }

    private static void print(String input, String[] partsOfInput) {
        Matcher matcherOfPrintFreeAgents = Pattern.compile("print\\s+free\\s+agents").matcher(input);
        Matcher matcherOfPrintStatsOfTeam = Pattern.compile("print\\s+stats\\s+of\\s+team\\s+(\\w+)").matcher(input);
        Matcher matcherOfPrintStatsOfPlayer = Pattern.compile("print\\s+stats\\s+of\\s+player\\s+(\\w+)\\s+(\\w+)").
                matcher(input);
        if (matcherOfPrintFreeAgents.find()) {
            League.PrintFreeAgents();
        } else if (matcherOfPrintStatsOfTeam.find()) {
            if (League.hasTeam(partsOfInput[4])) {
                League.PrintStatsOfTeam(partsOfInput[4]);
            } else {
                printInvalidTeam();
            }
        } else if (matcherOfPrintStatsOfPlayer.find()) {
            if (League.hasPlayer(partsOfInput[4], partsOfInput[5], League.allPlayers)) {
                League.PrintStatsOfPlayer(partsOfInput[4], partsOfInput[5]);
            } else {
                printInvalidPlayer();
            }
        } else {
            printInvalidCommand();
        }
    }

    private static void delete(String input, String[] partsOfInput) {

        Matcher matcher = Pattern.compile("(delete\\s+team\\s+)(\\w+)").matcher(input);
        if (matcher.find()) {
            if (League.hasTeam(partsOfInput[2])) {
                League.deleteTeam(partsOfInput[2]);
            } else {
                printInvalidTeam();
            }
        } else {
            printInvalidCommand();
        }
    }

    private static void create(Scanner scanner, String input, String[] partsOfInput) {

        Matcher matcherOfCreateTeam = Pattern.compile("(create\\s+team\\s+)(\\w+)").matcher(input);
        if (matcherOfCreateTeam.find()) {
            if (League.hasTeam(partsOfInput[2])) {
                out.println("a team exists with this name");
            } else {
                Team newTeam = new Team(partsOfInput[2]);
                League.addNewTeam(newTeam);
                out.println("team created");
            }
        } else {
            Matcher matcherOfCreatePlayer = Pattern.compile("(create\\s+player\\s+)(\\w+)\\s+(\\w+)\\s+" +
                    "(\\d+)\\s+(\\w{2})").matcher(input);
            if (matcherOfCreatePlayer.find()) {
                if (League.hasPlayer(partsOfInput[2], partsOfInput[3], League.allPlayers)) {
                    out.println("a player exists with this name");
                } else {
                    ArrayList<String> position = new ArrayList<>();
                    addPositions(position);
                    if (!position.contains(partsOfInput[5])) {
                        printInvalidCommand();
                    } else {
                        Player newPlayer = new Player(partsOfInput[2], partsOfInput[3], toInt(partsOfInput[4]),
                                partsOfInput[5]);
                        setPowers(scanner, newPlayer);
                        League.addNewPlayer(newPlayer);
                        out.println("player created");
                    }
                }
            } else {
                printInvalidCommand();
            }
        }
    }

    private static void addPositions(ArrayList<String> position) {
        position.add("GK");
        position.add("DF");
        position.add("MF");
        position.add("ST");
    }

    private static void printInvalidCommand() {
        out.println("invalid command");
    }

    private static void setPowers(Scanner scanner, Player player) {
        switch (player.getPosition()) {
            case "GK":
                out.println("Shot Saving:");
                player.setPower1(toInt(scanner.nextLine()));
                out.println("Reactions:");
                player.setPower2(toInt(scanner.nextLine()));
                out.println("Penalty Saving:");
                player.setPower3(toInt(scanner.nextLine()));
                break;
            case "DF":
                out.println("Strength:");
                player.setPower1(toInt(scanner.nextLine()));
                out.println("Aggression:");
                player.setPower2(toInt(scanner.nextLine()));
                out.println("Heading:");
                player.setPower3(toInt(scanner.nextLine()));
                break;
            case "MF":
                out.println("Passing:");
                player.setPower1(toInt(scanner.nextLine()));
                out.println("Shooting:");
                player.setPower2(toInt(scanner.nextLine()));
                out.println("Crossing:");
                player.setPower3(toInt(scanner.nextLine()));
                break;
            case "ST":
                out.println("Heading:");
                player.setPower1(toInt(scanner.nextLine()));
                out.println("Finishing:");
                player.setPower2(toInt(scanner.nextLine()));
                out.println("Penalties:");
                player.setPower3(toInt(scanner.nextLine()));
                break;
            default:
                break;
        }
    }

    private static int toInt(String string) {
        return Integer.parseInt(string);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        menu(scanner);
    }
}

