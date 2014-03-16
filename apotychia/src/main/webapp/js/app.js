window.App = Ember.Application.create({
    LOG_TRANSITIONS: true
});

App.Store = DS.Store.extend({
 revision: 12,
 adapter: 'DS.FixtureAdapter'
});

App.Person = DS.Model.extend({
    name: DS.attr('string')
});

App.Member = DS.Model.extend({
    name: DS.attr('string')
});


App.Person.FIXTURES = [
  {
    id: 1,
    name: "Andreas"
  }, {
    id: 2,
    name: "Ole"
  }, {
    id: 3,
    name: "Kent"
  }, {
    id: 4,
    name: "Annie"
  }, {
    id: 5,
    name: "Sid"
  }, {
    id: 6,
    name: "Emily"
  }, {
    id: 7,
    name: "Bonnie"
  }, {
    id: 8,
    name: "Tommy"
  }, {
    id: 9,
    name: "Knut"
  }
];


App.Member.FIXTURES = [
  {
    id: 1,
    name: "worker1"
  }, {
    id: 2,
    name: "woerk2"
  }, {
    id: 3,
    name: "worker3"
  }, {
    id: 4,
    name: "worker3"
  }, {
    id: 5,
    name: "worker3"
  }, {
    id: 6,
    name: "worker3"
  }, {
    id: 7,
    name: "worker3"
  }, {
    id: 8,
    name: "worker3"
  }, {
    id: 9,
    name: "worker9"
  }, {
    id: 10,
    name: "worker10"
  }, {
    id: 11,
    name: "worker11"
  }
];

App.Router.map(function() {
    this.route("calender", { path: "/calender" });
    this.route("new-event");
    this.route("watch-event");
});


App.CalenderRoute = Ember.Route.extend({
    model: function(params) {
        return Ember.RSVP.hash({
            persons: this.store.find('person'),
            members: this.store.find('member')
        })
    }
});

App.CalenderController = Ember.Controller.extend({

});

App.CalenderView = Ember.View.extend({
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

App.IndexRoute = Ember.Route.extend({
  redirect: function() {
    this.transitionTo('calender');
  }
});



