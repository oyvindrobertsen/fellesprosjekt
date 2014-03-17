App.Router.map(function() {
    this.resource('events', function() {
        this.resource('event', {path: ':event_id'});
        this.resource('add');
    });
    this.resource('me');
});

App.IndexRoute = Ember.Route.extend({
    redirect: function() {
        this.transitionTo('events');
    }
});

App.EventsIndexRoute = Ember.Route.extend({
    model: function() {
        return Ember.$.getJSON('/api/events');
    }
});

App.MeRoute = Ember.Route.extend({
    model: function() {
        return Ember.$.getJSON('/api/auth/me');
    }
});