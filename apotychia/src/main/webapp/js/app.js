window.App = Ember.Application.create({
    LOG_TRANSITIONS: true
});

//App.ApplicationAdapter = DS.RESTAdapter.extend({
//    namespace: 'api'
//});
//
//
//App.Store = DS.Store.extend({
//    adapter: App.ApplicationAdapter
//});
//
//App.ApplicationAdapter.map('App.Event', {
//    primaryKey: 'eventId'
//});

//// models
//
//App.Event = DS.Model.extend({
//    eventName: DS.attr('string'),
//    startTime: DS.attr('string'),
//    endTime: DS.attr('string'),
//    active: DS.attr('boolean'),
//    description: DS.attr('string'),
//    eventAdmin: DS.attr('string')
//});

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
    return Ember.$.getJSON('/api/events');
  }
});

App.NewRoute = Ember.Route.extend({
  model: function() {
    return {
      newEvent: {},
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
    content: {},
    isRoom: true,
    actions: {
        save: function() {
            var self = this;
            Ember.$.ajax({
                url: '/api/events',
                type: 'POST',
                dataType: 'xml/html/script/json',
                contentType: 'application/json',
                data: JSON.stringify({
                    eventName: this.get('eventName'),
                    startTime: '18-03-2014 09:00',
                    endTime: '18-03-2014 10:00',
                    eventAdmin: '',
                    description: this.get('description'),
                    active: true
                }),
                complete: function(data) {
                    window.location.replace('/#/event');
                }
            })
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