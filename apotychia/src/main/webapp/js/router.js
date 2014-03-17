App.Router.map(function() {
    this.resource('app');
    this.resource('me');
});

App.AppRoute = Ember.Route.extend({
    model: function() {
        return
    }
});

App.MeRoute = Ember.Route.extend({
    model: function() {
        return Ember.$.getJSON('/api/auth/me');
    }
});