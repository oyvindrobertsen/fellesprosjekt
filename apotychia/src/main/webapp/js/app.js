window.App = Ember.Application.create({
    LOG_TRANSITIONS: true
});

App.RESTAdapter = DS.RESTAdapter.extend({
    namespace: 'api'
});

App.Store = DS.Store.extend({
 revision: 12,
 adapter: 'DS.FixtureAdapter'
});

App.Store = DS.Store.extend({
    adapter: App.RESTAdapter
});

// models

App.Member = DS.Model.extend({
    name: DS.attr('string')
});

App.Person = DS.Model.extend({
    name: DS.attr('string')
});

App.CalenderEvent = DS.Model.extend({
    eventName: DS.attr('string')
    //startTime: DS.attr('date')
    //endTime: DS.attr('date')
    //isActive: DS.attr('boolean')
    //description: DS.attr('string')
    //eventAdmin: DS.attr('person')
});

// routes

App.Router.map(function() {
    this.resource('calender', { path: "/calender"}, function() {  // calender
      this.resource("newevent"); //calender/new
      this.resource('watch', { path: 'watch/:event_id' });  // calender/watch/2
      this.resource('edit', { path: 'edit/:event_id'}); // calender/edit/1
    });
    this.resource('me');
});

App.CalenderRoute = Ember.Route.extend({
    model: function() {
        return  this.store.find('calenderEvent')
    }
});

App.NeweventRoute = Ember.Route.extend({
    model: function(params) {
        return Ember.RSVP.hash({
            persons: this.store.find('person'),
            members: this.store.find('member'),
        })
    }
});


App.MeRoute = Ember.Route.extend({
  model: function() {
    return Ember.$.getJSON('/api/auth/me');
  }
});

// controller

App.NeweventController = Ember.ObjectController.extend({

    actions: {
      save: function(params) {
          var newCevent = App.CalenderEvent.create();
          newCevent.set('eventName', params.eventName);
      }.property('model.calenderEvent')
    }
});


// views

App.NeweventView = Ember.View.extend({
    didInsertElement: function() {
      // Enable popovers
      $("a[rel=popover]").popover({
        placement: 'left',
        html: true,
        content: function() {
            return $('#popover_content_wrapper').html();
    }
      }).click(function(e) {
        e.preventDefault();
      });
    }
});

// index route

App.IndexRoute = Ember.Route.extend({
  redirect: function() {
    this.transitionTo('calender');
  }
});