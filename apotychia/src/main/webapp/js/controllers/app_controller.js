(function () {
  App.AppController = Ember.Controller.extend({

    name: "andreas",

    actions: {
      getTitle: function () {
        var title = "nig";
        return title;
      }
    }
  });
});