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
    this.resource('userEvent', {path: ':id'});
  });

  this.resource('invites', { path: "/event/invites"}, function() {
    this.resource('viewInvite', { path: "/:id"})
  });
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
        // return Ember.$.getJSON('/api/events');
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
            rooms: Ember.$.getJSON('/api/rooms'),
            participants: [],
            room: null
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

App.ViewInviteRoute = Ember.Route.extend({
    model: function(params) {
        return Ember.RSVP.hash({
            event: Ember.$.getJSON('/api/events/' + params.id),
            attending: Ember.$.getJSON('/api/events/' + params.id + '/attending'),
            invited: Ember.$.getJSON('/api/events/' + params.id + '/invited'),
            declined: Ember.$.getJSON('/api/events/' + params.id + '/declined')
        });
    }
});


App.EditRoute = Ember.Route.extend({
    model: function(params) {
        return Ember.RSVP.hash({
            event: Ember.$.getJSON('/api/events/' + params.id),
            groups: Ember.$.getJSON('/api/groups'),
            attending: Ember.$.getJSON('/api/events/' + params.id + '/attending'),
            invited: Ember.$.getJSON('/api/events/' + params.id + '/invited'),
            declined: Ember.$.getJSON('/api/events/' + params.id + '/declined'),
            rooms: Ember.$.getJSON('/api/rooms'),
            users: Ember.$.getJSON('/api/auth/users'),
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

App.UserEventRoute = Ember.Route.extend({
    model: function(params) {
        return Ember.RSVP.hash({
            event: Ember.$.getJSON('/api/events/' + params.id),
            attending: Ember.$.getJSON('/api/events/' + params.id + '/attending'),
            invited: Ember.$.getJSON('/api/events/' + params.id + '/invited'),
            declined: Ember.$.getJSON('/api/events/' + params.id + '/declined')
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
        },
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
                    location: this.get('location'),
                    room: this.get('model.room'),
                    invited: this.get('model.participants')
                }),
                complete: function(data) {
                    self.transitionToRoute('/');
                }
            })
        },

        disablePlaceInput : function() {
            Ember.$('#placeinput').prop('disabled', true);
            <!-- ny kode -a-->
            this.set('location', null);
            <!-- end -->
        },

        enablePlaceInput : function() {
            Ember.$('#placeinput').prop('disabled', false);
            <!-- ny kode -a-->
            this.set('model.room', null);
            <!-- end -->
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
            this.set('model.room', object);
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
            var a = this.get('model.attending');
            var idx;
            for(idx = 0; idx < a.length; idx++){
                a[idx].type = ".User";
            }
            var a = this.get('model.invited');
            var idx;
            for(idx = 0; idx < a.length; idx++){
                a[idx].type = ".User";
            }

            var self = this;
            Ember.$.ajax({
                url: '/api/events/' + this.get('model.event.eventID'),
                type: 'PUT',
                dataType: 'xml/html/script/json',
                contentType: 'application/JSON',
                data: JSON.stringify({
                    eventId: this.get('model.event.eventID'),
                    eventName: this.get('model.event.eventName'),
                    startTime: this.get('date') + ' ' + this.get('startTime'),
                    endTime: this.get('date') + ' ' + this.get('endTime'),
                    eventAdmin: this.get('model.event.eventAdmin'),
                    description: this.get('model.event.description'),
                    active: this.get('model.event.active'),
                    invited: this.get('model.invited'),
                    attending: this.get('model.attending'),
                    room: this.get('model.event.room'),
                    location: this.get('model.event.location')
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
        },
        addToInvited: function(object) {
            if (object.username) {
                object.type = ".User";
            } else {
                object.type = ".Group";
            }
            this.get('model.invited').pushObject(object);
            var a = this.get('model.attending');
            var idx;
            for(idx = 0; idx < a.length; idx++){
                a[idx].type = ".User";
            }
        },
        removeFromAttending: function(object) {
            this.get('model.attending').removeObject(object);
        },
        removeFromInvited: function(object) {
            this.get('model.invited').removeObject(object);
            var a = this.get('model.attending');
            var idx;
            for(idx = 0; idx < a.length; idx++){
                a[idx].type = ".User";
            }
        },
        addRoomToEvent: function(object) {
            this.set('model.event.room', object);
        },
        disablePlaceInput : function() {
            Ember.$('#placeinput').prop('disabled', true);
            <!-- ny kode -a-->
            this.set('model.event.location', null);
            <!-- end -->
        },
        enablePlaceInput : function() {
            Ember.$('#placeinput').prop('disabled', false);
            <!-- ny kode -a-->
            this.set('model.event.room', null);
            <!-- end -->
        },
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
        <!-- popover hides on click outside box -->
        $('body').on('click', function (e) {
            $('[rel=popover], [rel=popover2]').each(function () {
                // hide any open popovers when the anywhere else in the body is clicked
                if (!$(this).is(e.target) && $(this).has(e.target).length === 0 && $('.popover').has(e.target).length === 0) {
                    $(this).popover('hide');
                }
            });
        });
        <!-- end -->
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
      <!-- popover hides on click outside box -->
      $('body').on('click', function (e) {
          $('[rel=popover], [rel=popover2]').each(function () {
              // hide any open popovers when the anywhere else in the body is clicked
              if (!$(this).is(e.target) && $(this).has(e.target).length === 0 && $('.popover').has(e.target).length === 0) {
                  $(this).popover('hide');
              }
          });
      });
      <!-- end -->
      }
});

// index route

App.IndexRoute = Ember.Route.extend({
    redirect: function() {
        this.transitionTo('event');
    }
});