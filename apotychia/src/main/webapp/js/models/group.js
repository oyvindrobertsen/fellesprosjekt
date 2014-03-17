App.Group = DS.Model.extend({
    groupName: DS.attr('string'),
    members: DS.hasMany('person')
});