App.Person = DS.Model.extend({
	username: DS.attr('string'),
	firstName: DS.attr('string'),
	lastName: DS.attr('string'),
	fullName: function() {
        return this.get('firstName') + ' ' + this.get('lastName');
	}.property('firstName', 'lastName'),
	email: DS.attr('string'),
});

App.RESTAdapter.map('App.Person', {
    primaryKey: 'username'
});