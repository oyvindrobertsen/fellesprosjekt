App.Router.map(function() {
    this.route("index", {path: "/"});
    this.route("slett", {path: "/"});
    this.resource("photos", {path: "/photos"}, function() {
        this.route("selectedPhoto", {path: ":photo_id"})
    });
});

App.IndexRoute = Ember.Route.extend({
    redirect: function() {
        this.transitionTo('photos');
    }
});