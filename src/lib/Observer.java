package lib;

import database.models.Model;

public interface Observer<T extends Model<T>> {

    public void update(T o);

}
