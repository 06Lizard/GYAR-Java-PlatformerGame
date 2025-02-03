// boolians are inmutable in java meaning you can't refrence to them from other classes or feilds
// this means if you pass a bool you always copy it instead of refrencing
// however with this wrapper you can refrence an object containing a bool :)
// workaround teehee!!! >:D
class BoolWrapper{
    public boolean state;
    public BoolWrapper(boolean state) {
        this.state = state;
    }
}