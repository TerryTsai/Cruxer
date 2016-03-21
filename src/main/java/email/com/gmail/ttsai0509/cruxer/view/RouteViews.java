package email.com.gmail.ttsai0509.cruxer.view;

public interface RouteViews {
    interface Id {}

    interface Date {}

    interface Name {}

    interface Account {}

    interface Thumbnail {}

    interface WallInstances {}

    interface Complete extends Id, Name, Date, Thumbnail,
            Account, AccountViews.Id, AccountViews.Username,
            WallInstances, WallInstanceViews.Id, WallInstanceViews.Pose, WallInstanceViews.Material,
            WallInstanceViews.Wall, WallViews.Id, WallViews.Model,
            WallInstanceViews.HoldInstances, HoldInstanceViews.Id, HoldInstanceViews.Pose, HoldInstanceViews.Material,
            HoldInstanceViews.Hold, HoldViews.Id, HoldViews.Model,
            PoseViews.Pose, MaterialViews.Material {}

    interface Details extends Id, Name, Date, Account, Thumbnail, AccountViews.Id, AccountViews.Username {}
}
