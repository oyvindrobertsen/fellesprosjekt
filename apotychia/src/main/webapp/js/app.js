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
  this.resource('me');
});

App.EventRoute = Ember.Route.extend({
    model: function() {
        // return Ember.$.getJSON('/api/events');
    }
});

App.NewRoute = Ember.Route.extend({
    model: function() {
        return Ember.RSVP.hash({
            users: Ember.$.getJSON('/api/auth/users'),
            rooms: null, //Ember.$.getJSON('/api/auth/rooms'),
            participants: [],
            room: null
        });
    }
});

App.EditRoute = Ember.Route.extend({
    model: function(params) {
        return Ember.$.getJSON('/api/events/' + params.id);
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
                    active: true,
                    invited: this.get('model.participants')
                }),
                complete: function(data) {
                    self.transitionToRoute('/');
                }
            })
        },

        disablePlaceInput : function() {
            Ember.$('#placeinput').prop('disabled', true);
        },

        enablePlaceInput : function() {
            Ember.$('#placeinput').prop('disabled', false);
        },
        addToParticipants: function(object) {
            if (object.username) {
                object.type = ".User";
            } else {
                object.type = ".Group";
            }
            this.get('model.participants').pushObject(object);
        },
        removeFromParticipants: function(object) {
            this.get('model.participants').removeObject(object);
        },
        addRoomToEvent: function(object) {
            this.get('model.room').pushObject(object);
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
            var self = this;
            Ember.$.ajax({
                url: '/api/events/' + this.get('model.eventID'),
                type: 'PUT',
                dataType: 'xml/html/script/json',
                contentType: 'application/JSON',
                data: JSON.stringify({
                    eventID: this.get('model.eventID'),
                    eventName: this.get('model.eventName'),
                    startTime: this.get('date') + " " + this.get('startTime'),
                    endTime: this.get('date') + " " + this.get('endTime'),
                    eventAdmin: this.get('model.eventAdmin'),
                    description: this.get('model.description'),
                    active: this.get('model.active')
                }),
                complete: function() {
                    self.transitionToRoute('/');
                }
            });
        },
        delete: function() {
            var self = this;
            Ember.$.ajax({
                url: '/api/events/' + this.get('model.eventID'),
                type: 'DELETE',
                complete: function() {
                    self.transitionToRoute('/');
                }
            });
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
            container: 'body',
            content: function() {
                return $('#users-popover').html();
            }
        }).click(function(e) {
            e.preventDefault();
        });
        $("label[rel=popover2]").popover({
            placement: 'left',
            html: true,
            container: 'body',
            content: function() {
                return $('#rooms-popover').html();
            }
        }).click(function(e) {
            e.preventDefault();
        });
    }
});

App.EditView = Ember.View.extend({
  didInsertElement: function() {
    // Enable popovers
    $("a[rel=popover]").popover({
      placement: 'left',
      html: true,
      container: 'body',
      content: function() {
        return $('#users-content-wrapper').html();
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