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

App.Event = DS.Model.extend({
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
    this.resource("view", {path: ":id"});
    this.resource("new"); //calender/new
    this.resource('edit', { path: 'edit/:id'}); // calender/edit/1
  });
  this.resource('me');
});

App.EventRoute = Ember.Route.extend({
  model: function() {
    return this.store.find('event');
  }
});

App.NewRoute = Ember.Route.extend({
  model: function() {
    return {
      newEvent: App.Event.createRecord({isActive: true}),
      members: this.store.find('member'),
      persons: this.store.find('person')
    }
  }
});

App.EditRoute = Ember.Route.extend({
  model: function(params) {
    return {
      editEvent: this.store.find('event', params.id),
      members: this.store.find('member'),
      persons: this.store.find('person')
    }
  }
});

App.ViewRoute = Ember.Route.extend({
  model: function(params) {
    return {
      viewEvent: this.store.find('event', params.id),
      members: this.store.find('member')
    }
  }
});

App.MeRoute = Ember.Route.extend({
  model: function() {
    return Ember.$.getJSON('/api/auth/me');
  }
});

// controller

App.NewController = Ember.ObjectController.extend({
  isRoom: true,
  actions: {
    save: function() {
        Ember.$.post('/api/events', this.get('model.newEvent'));
    },

    disablePlaceInput : function() {
      this.set('isRoom', false);
      console.log(isRoom);
    },

    enablePlaceInput : function() {
      this.set('isRoom', true);
      console.log(isRoom);
    }
  }
});

App.ViewController = Ember.ObjectController.extend({
  actions: {
    attend: function() {
        // todo
    },

    notAttend: function() {
        //todo
    }
  }
});

App.EditController = Ember.ObjectController.extend({
  actions: {
    saveEdit: function() {
      // todo
    }
  }
});

// views

App.NewView = Ember.View.extend({
  didInsertElement: function() {
    // Enable popovers
    $("label[rel=popover]").popover({
      placement: 'left',
      html: true,
      container: 'body',
      content: function() {
        return $('#popover-content-wrapper').html();
      }
    }).click(function(e) {
      e.preventDefault();
    });
  }
});

App.EditView = Ember.View.extend({
  didInsertElement: function() {
    // Enable popovers
    $("label[rel=popover]").popover({
      placement: 'left',
      html: true,
      container: 'body',
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