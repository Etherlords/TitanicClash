package geom;

import player.MoveData;

public class PathMathematic
{

    public PathMathematic()
    {

    }

    public double calculateTravelTime(MoveData moveData)
    {
        double distance = moveData.startPoint.distance(moveData.endPoint);


        return (distance / moveData.speed) * 1000;
    }

    public Point calculateCurrentPosition(MoveData moveData)
    {
        if (moveData.startTime + moveData.travelTime < currentTime())
            return moveData.endPoint;

        double timeDelta = currentTime() - moveData.startTime;
        double partOfPath = moveData.travelTime / timeDelta;

        Point startPoint = moveData.startPoint.clone();
        Point currentPoint = moveData.endPoint.clone().subtract(startPoint);

        currentPoint.x /= partOfPath;
        currentPoint.y /= partOfPath;

        currentPoint = currentPoint.add(startPoint);

        return currentPoint;
    }

    long currentTime()
    {
        return System.currentTimeMillis();
    }

}