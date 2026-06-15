package d3.soar.main;

public enum Command {
    REGISTER,
    LOGIN,
    LOGOUT,
    SEARCH_FLIGHT,
    HELP,
    EXIT,
    INVALID;

    ///@brief Returns the corresponding Command
    public static Command fromString(String input){
        try{
            return valueOf(input.trim().toUpperCase());
        }catch (Exception e){
            return INVALID;
        }
    }

    ///@brief Prints out all available commands
    public static String help(){
        StringBuilder out = new StringBuilder();
        for(Command cmd: Command.values()){
            String app = "- " + cmd.name() + "\n";
            out.append(app);
        }
        return out.toString();
    }
}
