package mazeGenerator;

public enum dirs implements java.io.Serializable{

    right(new int[]{1, 0}), left(new int[]{-1, 0}), up(new int[]{0, -1}), down(new int[]{0, 1});

    private int[] transform;

    public int[] getTransform() {
        return transform;
    }

    public int getTransX() {
        return transform[0];
    }

    public int getTransY() {
        return transform[1];
    }

    public dirs getOpposite() {
        for (dirs direction : dirs.values())
            if ((getTransX() == direction.getTransX() * -1) && (getTransY() == direction.getTransY() * -1))
                return direction;
        System.err.println("error getting opposite direction");
        return null;
    }

    dirs(int[] transform) {
        this.transform = transform;
    }
}
