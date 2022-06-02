package lib;

public interface Observable {

    public void addOberver(Observer observer);

    public void removeOberver(Observer observer);

    public void notifyAllObervers();

}
