window.App = Ember.Application.create({
    LOG_TRANSITIONS: true
});

App.Store = DS.Store.extend({
 revision: 12,
 adapter: 'DS.FixtureAdapter'
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
    this.route('calender', { path: '/:action_id' }, function() {  // calender/watch or calender/edit
      this.route('event', { path: '/:event_id' });  // calender/watch/2 or calender/edit/5
    });
    this.route("newevent", { path: "/newevent" }); //calender/new
});

App.ApplicationRoute = Ember.Route.extend({
    model: function() {
        return  this.store.find('calenderEvent')
    }
});


App.NeweventRoute = Ember.Route.extend({
    model: function(params) {
        return Ember.RSVP.hash({
            persons: this.store.find('person'),
            members: this.store.find('member')
        })
    }
});

// controller

App.NeweventController = Ember.ArrayController.extend({
  actions: {
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
    this.transitionTo('newevent');
  }
});



