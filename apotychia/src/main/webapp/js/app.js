window.App = Ember.Application.create({
    LOG_TRANSITIONS: true
});

App.Router.reopen({
    rootURL: '/'
})