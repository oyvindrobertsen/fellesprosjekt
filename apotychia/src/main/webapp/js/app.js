window.App = Ember.Application.create({
    LOG_TRANSITIONS: true
});

App.ApplicationAdapter = DS.RESTAdapter.extend({
    namespace: 'api'
});

App.Store = DS.Store.extend({
    adapter: App.ApplicationAdapter
});

App.ApplicationAdapter.map('App.Event', {
    primaryKey: 'eventId'
});

// models

App.Event = DS.Model.extend({
    eventName: DS.attr('string'),
    startTime: DS.attr('string'),
    endTime: DS.attr('string'),
    active: DS.attr('boolean'),
    description: DS.attr('string'),
    eventAdmin: DS.attr('string')
});

// routes

App.Router.map(function() {
    this.resource('event', { path: "/event"}, function() {  // event
        this.resource("new"); //event/new
        this.resource('edit', { path: 'edit/:event_id'}); // event/edit/1
    });
    this.resource('me');
});

App.EventRoute = Ember.Route.extend({
    model: function() {
        return this.store.find('event');
    }
});


App.MeRoute = Ember.Route.extend({
    model: function() {
        return Ember.$.getJSON('/api/auth/me');
    }
});

// controller

App.NewController = Ember.ObjectController.extend({
    content: {},
    actions: {
        save: function() {
            App.Event.createRecord({
                eventName: this.get('eventName'),
                startTime: '18-03-2014 09:00',
                endTime: '18-03-2014 10:00',
                eventAdmin: '',
                description: '',
                isActive: true
            }).save();
        }
    }
});

// views

App.NewView = Ember.View.extend({
    didInsertElement: function() {
        // Enable popovers
        $("a[rel=popover]").popover({
            placement: 'left',
            html: true,
            content: function() {
                return $('#popover-content-wrapper').html();
        }
        }).click(function(e) {
            e.preventDefault();
        });
    }
});

// index route

App.IndexRoute = Ember.Route.extend({
    redirect: function() {
        this.transitionTo('event');
    }
});