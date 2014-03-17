$('.popover-markup>.trigger').popover({
	placement:'left',
    html: true,
    title: function () {
        return $(this).parent().find('.head').html();
    },
    content: function () {
        return $(this).parent().find('.content').html();
    }
});

window.App = Ember.Application.create({
    LOG_TRANSITIONS: true
});

App.RESTAdapter = DS.RESTAdapter.extend({
    namespace: 'api'
});

App.Store = DS.Store.extend({
    adapter: App.RESTAdapter
});
