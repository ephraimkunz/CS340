package Communication;

import android.os.Handler;
import android.os.Looper;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import ClientModel.ClientModel;
import Services.ClientGameService;
import common.Endpoints;
import common.ICommand;

/**
 * The poller is keeps this client up to date with the server. It polls the server periodically,
 * recieving a list of commands to bring it up to date with the server state and then executing
 * these commands. The network access of the poller takes place on a background thread to prevent
 * blocking on the UI.
 */
public class Poller {
    private static Poller _instance = new Poller();

    private int pollPeriodMS = 1000; // set poll period, default at 1 second
    private static ScheduledExecutorService _threadInstance; // separate thread to complete polling

    /**
     * There are two types of polling that the Poller supports: polling for commands to execute and
     * polling for changes in the list of games. Only one type of poll can take place at a time.
     */
    protected enum pollTypes {
        COMMAND,
        GAME
    }

    private static pollTypes currentPollType = pollTypes.GAME;

    /**
     * Because Poller is a singleton, it has a private constructor. To get the shared instance of
     * Poller, call the static getInstance().
     */
    private Poller(){}

    /**
     * Poller is a singleton. Get instance returns this singleton instance.
     *
     * @return the singleton instance
     *
     * @postcondition the instance of poller returned is ready to be used for polling
     */
    public static Poller getInstance() {
        return _instance;
    }


    /**
     * Starts polling the server for commands. Executing an ordered list of commands from the server
     * is how the each client stays in sync with it (it is the source of all truth).
     *
     * @precondition The correct server IP address and port number has been set in the ClientCommunicator
     * singleton.
     *
     * @precondition The player id of the player represented by this client has been set in the
     * ClientModel. This is done by the register or login functions.
     *
     * @precondition The player history position has been set in the ClientModel. This should have
     * been set to the default value of 0 when the ClientModel was constructed.
     *
     * @precondition Any previously running poller has been stopped by calling either the
     * stopGamePoll or stopCommandPoll methods.
     *
     * @postcondition The poller polls the server at the poll endpoint every pollPeriodMS, executing
     * any commands it receives while polling.
     */
    public void startCommandPoll() {
        if(_threadInstance != null)
        {
            _threadInstance.shutdown();
        }
        currentPollType = pollTypes.COMMAND; // set polling type
        _threadInstance = Executors.newSingleThreadScheduledExecutor(); //create polling thread
        ScheduledFuture future = _threadInstance.scheduleWithFixedDelay(new PollerThread(), 0, pollPeriodMS, TimeUnit.MILLISECONDS); //start polling thread
    }

    /**
     * Shuts down the poller that has been polling for commands.
     *
     * @precondition startCommandPoll has been called previously and hasn't yet been stopped.
     *
     * @postcondition polling stops. Internal state of poller does not change.
     */
    public void stopCommandPoll() {
        _threadInstance.shutdown();
    }

    /**
     * Starts polling the server for an updated list of games.
     * @precondition The correct server IP address and port number has been set in the ClientCommunicator
     * singleton.
     *
     * @precondition Any previously running poller has been stopped by calling either the
     * stopGamePoll or stopCommandPoll methods.
     *
     * @postcondition The list of available games (available to join) on the server will be set in
     * the ClientModel.
     */
    public void startGamePoll()
    {
        if(_threadInstance != null)
        {
            _threadInstance.shutdown(); //shut down polling thread if it is active
        }
        currentPollType = pollTypes.GAME; // set polling type
        _threadInstance = Executors.newSingleThreadScheduledExecutor(); // create polling thread
        ScheduledFuture future = _threadInstance.scheduleWithFixedDelay(new PollerThread(), 0, pollPeriodMS, TimeUnit.MILLISECONDS); // start polling thread
    }

    /**
     * Shuts down the poller that has been polling for an updated game list.
     *
     * @precondition startGamePoll has been called previously and hasn't yet been stopped.
     *
     * @postcondition polling stops. Internal state of poller does not change.
     */
    public void stopGamePoll()
    {
        _threadInstance.shutdown();
    }

    /**
     * Calls ClientCommunicator to get command list.
     *
     * @return command list if successful, empty command list otherwise.
     */
    private List<ICommand> fetchCommands()
    {
        ClientCommunicator communicator = ClientCommunicator.getInstance(); // get communicator instance
        String playerID = ClientModel.getInstance().getUser().getId();
        int historyPos = ClientModel.getInstance().getUser().getHistoryPosition();
        Class resultClass = ICommand[].class;

        ICommand[] commandArray = (ICommand[]) communicator.get(Endpoints.POLL_ENDPOINT, "", playerID + "\n" + historyPos, resultClass); // send command, get results
        ClientModel.getInstance().getUser().setHistoryPosition(historyPos + commandArray.length);
        return Arrays.asList(commandArray);
    }

    /**
     * Calls ClientCommunicator to get game list
     *
     * @return game list if successful, empty game list otherwise.
     */
    private List<String> fetchGames()
    {
        ClientCommunicator communicator = ClientCommunicator.getInstance();

        Class resultClass = String[].class;
        String[] gameArray = (String[]) communicator.get(Endpoints.GAME_LIST_ENDPOINT, "authToken", "", resultClass); //send command, get result

        return Arrays.asList(gameArray);
    }
}
