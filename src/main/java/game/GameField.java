package game;

public class GameField extends Thread {

  /*  HashMap players = new HashMap<String, Player>();
    Vector playerKeys = new Vector();
    private Server server;
    private PathMathematic pathMath = new PathMathematic();

    public GameField(Server server)
    {
        this.server = server;
    }

    @Override
    public void run()  {
        super.run();
        while (true)
        {
            try
            {
                sleep(1000);
                checkForCollision();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

        }

    }

    private void checkForCollision()
    {
        for(int i = 0; i < playerKeys.size(); i++)
        {
            Player currentPlayer = (Player) players.get(playerKeys.get(i));

            if(!currentPlayer.isConnected)
                continue;


        }
    }

    public void onPlayerAdded(PlayerConnection newPlayer) throws IOException {


        Player player = new Player();
        player.id = newPlayer.id;
        player.isConnected = true;
        player.moveData.startPoint = new Point(100, 100);
        player.moveData.endPoint = new Point(100, 100);

        players.put(player.id, player);
        playerKeys.add(player.id);

        BaseCommand addedCommand = CommandsFactory.getInstance().build(Commands.PLAYER_ADDED, newPlayer.id);

        for(int i = 0; i < playerKeys.size(); i++)
        {
            Player currentPlayer = (Player) players.get(playerKeys.get(i));

            if(currentPlayer == player || !currentPlayer.isConnected)
                continue;

            MoveCommand currentPositionCommand = new MoveCommand();
            currentPositionCommand.fromPlayerData(currentPlayer);
            currentPositionCommand.serverTime = System.currentTimeMillis();
            newPlayer.send(currentPositionCommand.toString());

        }

        server.notifyClients(newPlayer, addedCommand);
    }

    public void onPlayerSendCommand(PlayerConnection playerConnection, BaseCommand command) throws IOException {

        if(command.command == Commands.MOVE)
            handlMoveCommand((MoveCommand) command);
        else if(command.command == Commands.CAST_ABILITY)
            handlCastCommand((CastAbility) command);

        server.notifyClients(playerConnection, command);
    }

    private void handlCastCommand(CastAbility command){
        Player player = (Player) players.get(command.clientID);

        command.startPoint = pathMath.calculateCurrentPosition(player.moveData);
        double travelTime = pathMath.calculateTravelTime(player.moveData);
        command.travelTime = travelTime;
        command.startTime = System.currentTimeMillis();
    }

    private void handlMoveCommand(MoveCommand command) {
        Player player = (Player) players.get(command.clientID);

        command.startPoint = pathMath.calculateCurrentPosition(player.moveData);

        player.moveData.endPoint = command.endPoint;
        player.moveData.startPoint = command.startPoint;

        double travelTime = pathMath.calculateTravelTime(player.moveData);

        command.travelTime = travelTime;
        command.startTime = System.currentTimeMillis();

        player.moveData.travelTime = travelTime;
        player.moveData.startTime = command.startTime;
    }

    public void onPlayerLeave(PlayerConnection playerConnection) throws IOException {
        Player player = (Player)players.get(playerConnection.id);

        if(player == null)
            return;

        player.isConnected = false;

        BaseCommand cmd = new BaseCommand();
        cmd.command = Commands.REMOVE;
        cmd.clientID = playerConnection.id;

        server.notifyClients(playerConnection, cmd);
    }              */
}
