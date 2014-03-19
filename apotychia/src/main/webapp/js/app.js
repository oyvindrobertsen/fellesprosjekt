window.App = Ember.Application.create({
    LOG_TRANSITIONS: true
});

// routes

App.Router.map(function() {
  this.resource('event', { path: "/event"}, function() {  // calender
    this.resource("view", {path: ":id"});
    this.resource("new"); //calender/new
    this.resource('edit', { path: 'edit/:id'}); // calender/edit/1
  });
  this.resource('invites', { path: "/event/invites"});
  this.resource('me');
});

App.EventRoute = Ember.Route.extend({
    model: function() {
        return Ember.$.getJSON('/api/events');
    }
});

App.NewRoute = Ember.Route.extend({
    model: function() {
        return {};
    }
});

// Torgrim Edit
App.InvitesRoute = Ember.Route.extend({
    model: function() {
        return Ember.RSVP.hash({
            invites: Ember.$.getJSON('/api/events/invites')
        });   
    }
     
});

// End Torgrim edit

App.EditRoute = Ember.Route.extend({
    model: function(params) {
        return Ember.$.getJSON('/api/events/' + params.id)
    }
});

App.ViewRoute = Ember.Route.extend({
    model: function(params) {
        return Ember.$.getJSON('/api/events/' + params.id)
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
                    startTime: this.get('date') + " " + this.get('startTime'),
                    endTime: this.get('date') + " " + this.get('endTime'),
                    eventAdmin: '',
                    description: this.get('description'),
                    active: true
                }),
                complete: function(data) {
                    self.transitionToRoute('/');
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
    date: function() {
        return Ember.String.w(this.get('model.startTime'))[0];
    }.property('model.duration'),
    startTime: function() {
        return Ember.String.w(this.get('model.startTime'))[1];
    }.property('model.startTime'),
    endTime: function() {
        return Ember.String.w(this.get('model.endTime'))[1];
    }.property('model.endTime'),
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