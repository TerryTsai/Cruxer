package email.com.gmail.ttsai0509.cruxer.view;

import email.com.gmail.ttsai0509.cruxer.model.Account;

public interface RouteViews {
    interface Id {}

    interface Date {}

    interface Name {}

    interface Account {}

    interface Thumbnail {}

    interface WallInstances {}

    interface HoldInstances {}

    interface Standard extends Id, Name, Date,
            Account, Thumbnail, HoldInstances, WallInstances,
            AccountViews.Id, AccountViews.Username,
            HoldInstanceViews.Id, HoldInstanceViews.Location, HoldInstanceViews.Hold, HoldViews.Id, HoldViews.Model,
            WallInstanceViews.Id, WallInstanceViews.Location, WallInstanceViews.Wall, WallViews.Id, WallViews.Model {}
}
