package be.uantwerpen.sc.services;

import be.uantwerpen.sc.controllers.JobController;
import be.uantwerpen.sc.controllers.SimulationController;
import be.uantwerpen.sc.models.Job;
import be.uantwerpen.sc.tools.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Thomas on 25/02/2016.
 */
@Service
public class TerminalService
{
    private Terminal terminal;
    @Autowired
    private JobController jobController;
    @Autowired
    private SimulationController simulationController;

    public TerminalService()
    {
        terminal = new Terminal()
        {
            @Override
            public void executeCommand(String commandString)
            {
                parseCommand(commandString);
            }
        };
    }

    public void systemReady()
    {
        terminal.printTerminal(" :: SmartCity Core - 2016 ::  -  Developed by: Huybrechts T., Janssens A., Joosens D., Vervliet N.");
        terminal.printTerminal("Type 'help' to display the possible commands.");

        terminal.activateTerminal();
    }

    private void parseCommand(String commandString)
    {
        String command = commandString.split(" ", 2)[0].toLowerCase();

        switch(command)
        {
            case "job":
                try {
                    String command2 = commandString.split(" ", 2)[1].toLowerCase();
                    String robot = command2.split(" ", 2)[0].toLowerCase();
                    String job = command2.split(" ", 2)[1].toLowerCase();
                    System.out.println(robot + " do " + job);
                    try {
                        int robotId = Integer.parseInt(robot);
                        sendJobs(robotId, job);
                    } catch (NumberFormatException e) {
                        terminal.printTerminalError(e.getMessage());
                        terminal.printTerminal("Usage: navigate start end");
                    }
                }catch (Exception e){
                    terminal.printTerminal(e.getMessage());
                }
                break;
            case "simulate":
                try {
                    String command2 = commandString.split(" ", 2)[1].toLowerCase();
                    if(command2.equals("true"))
                        simulationController.setSimulation("http://localhost:8080/simulate/",true);
                    else
                        simulationController.setSimulation("http://localhost:8080/simulate/",false);
                }catch(ArrayIndexOutOfBoundsException e){
                    terminal.printTerminal("error");
                }
                break;
            case "exit":
                exitSystem();
                break;
            case "help":
            case "?":
                printHelp("");
                break;
            default:
                terminal.printTerminalInfo("Command: '" + command + "' is not recognized.");
                break;
        }
    }

    private void exitSystem()
    {
        System.exit(0);
    }

    private void printHelp(String command)
    {
        switch(command)
        {
            default:
                terminal.printTerminal("Available commands:");
                terminal.printTerminal("-------------------");
                terminal.printTerminal("'job {robotId} {doSomething}': send a command to the robot");
                terminal.printTerminal("'simulate {true/false}' : activte/deactivate robot simulator mode.");
                terminal.printTerminal("'exit' : shutdown the server.");
                terminal.printTerminal("'help' / '?' : show all available commands.\n");
                break;
        }
    }

    private void sendJobs(int robotID, String job){

        switch (robotID)
        {
            case 1:
                jobController.sendJob("http://localhost:8080/job/", job);
                break;
            default:
                System.out.println("Robot not found");
                break;
        }
    }
}
