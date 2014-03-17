window.App = Ember.Application.create({
    LOG_TRANSITIONS: true
});

App.Store = DS.Store.extend({
 revision: 12,
 adapter: DS.FixtureAdapter
});

// models

App.Member = DS.Model.extend({
    name: DS.attr('string')
});

App.Person = DS.Model.extend({
    name: DS.attr('string')
});

App.CalenderEvent = DS.Model.extend({
    eventName: DS.attr('string'),
    startTime: DS.attr('date'),
    endTime: DS.attr('date'),
    isActive: DS.attr('boolean'),
    description: DS.attr('string'),
    eventAdmin: DS.attr('person')
});

// routes

App.Router.map(function() {
    this.resource('event', { path: "/event"}, function() {  // calender
      this.resource("new"); //calender/new
      this.resource('edit', { path: 'edit/:event_id'}); // calender/edit/1
    });
    this.resource('me');
});

App.CalenderRoute = Ember.Route.extend({
    model: function() {
        return this.store.find('calenderEvent');
    }
});

App.NeweventRoute = Ember.Route.extend({
    model: function() {
        return {
            newEvent: App.CalenderEvent.createRecord({isActive: true}),
            members: this.store.find('member'),
            persons: this.store.find('person')
        }
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
      save: function() {
        Ember.$.post('/api/events', this.get('model.newEvent'));
      }
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
    this.transitionTo('calender');
  }
});