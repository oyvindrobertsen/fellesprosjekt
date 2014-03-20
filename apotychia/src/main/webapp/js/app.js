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
  this.resource('user', { path: 'event/user/:username'}, function() {
    this.resource('view', {path: ':id'});
  });

  this.resource('invites', { path: "/event/invites"});
  this.resource('me');
});

App.IndexRoute = Ember.Route.extend({
    model: function() {
        return Ember.RSVP.hash({
            users: Ember.$.getJSON('/api/auth/users')
        })
    }
});


App.EventRoute = Ember.Route.extend({
    model: function() {
        return Ember.RSVP.hash({
            events: Ember.$.getJSON('/api/events'),
            users: Ember.$.getJSON('/api/auth/users')
        });
    }
});

App.NewRoute = Ember.Route.extend({
    model: function() {
        return Ember.RSVP.hash({
            users: Ember.$.getJSON('/api/auth/users'),
            groups: Ember.$.getJSON('/api/groups'),
            participants: []
        });
    }
});

App.InvitesRoute = Ember.Route.extend({
    model: function() {
        return Ember.RSVP.hash({
            invites: Ember.$.getJSON('/api/events/invites')
        });   
    }
     
});


App.EditRoute = Ember.Route.extend({
    model: function(params) {
        return Ember.RSVP.hash({
            event: Ember.$.getJSON('/api/events/' + params.id),
            attending: Ember.$.getJSON('/api/events/' + params.id + '/attending'),
            invited: Ember.$.getJSON('/api/events/' + params.id + '/invited'),
            declined: Ember.$.getJSON('/api/events/' + params.id + '/declined')
        });
    }
});

App.ViewRoute = Ember.Route.extend({
    model: function(params) {
        return Ember.RSVP.hash({
            event: Ember.$.getJSON('/api/events/' + params.id),
            attending: Ember.$.getJSON('/api/events/' + params.id + '/attending'),
            invited: Ember.$.getJSON('/api/events/' + params.id + '/invited'),
            declined: Ember.$.getJSON('/api/events/' + params.id + '/declined')
        });
    }
});

App.UserRoute = Ember.Route.extend({
    model: function(params) {
        return Ember.RSVP.hash({
            events: Ember.$.getJSON('/api/events/user/' + params.username),
            users: Ember.$.getJSON('/api/auth/users')
        });
    }
});

App.MeRoute = Ember.Route.extend({
    model: function() {
        return Ember.$.getJSON('/api/auth/me');
    }
});

// controller

App.EventController = Ember.ObjectController.extend({
    calendarToView: null,
    actions: {
        viewCalendar: function() {
            if (this.get('calendarToView')) {
                this.transitionToRoute('/event/user/' + this.get('calendarToView'));
            }
            return;
        }
    }
});

App.UserController = Ember.ObjectController.extend({
    calendarToView: null,
    actions: {
        viewCalendar: function() {
            if (this.get('calendarToView')) {
                this.transitionToRoute('/event/user/' + this.get('calendarToView'));
            }
            return;
        }
    }
});

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
                object.type = "user";
            } else {
                object.type = "group";
            }
            this.get('model.participants').pushObject(object);
        },
        removeFromParticipants: function(object) {
            this.get('model.participants').removeObject(object);
        }
    }
});

App.ViewController = Ember.ObjectController.extend({
    actions: {
        attend: function() {
            var self = this;
            Ember.$.ajax({
                url: '/api/events/' + this.get('model.event.eventID') + '/attend',
                type: 'POST',
                dataType: 'xml/html/script/json',
                contentType: 'application/JSON',
                complete: function() {
                    self.transitionToRoute('/');
                }
            });
        },
        decline: function() {
            var self = this;
            Ember.$.ajax({
                url: 'api/events/' + this.get('model.event.eventID') + '/decline',
                type: 'POST',
                dataType: 'xml/html/script/json',
                contentType: 'application/JSON',
                complete: function() {
                    self.transitionToRoute('/');
                }
            })
        }
    }
});




App.EditController = Ember.ObjectController.extend({
    date: function() {
        return Ember.String.w(this.get('model.event.startTime'))[0];
    }.property('model.duration'),
    startTime: function() {
        return Ember.String.w(this.get('model.event.startTime'))[1];
    }.property('model.startTime'),
    endTime: function() {
        return Ember.String.w(this.get('model.event.endTime'))[1];
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
                url: '/api/events/' + this.get('model.event.eventID'),
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