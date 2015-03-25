Ext.define("MyApp.controller.ViewController", {
    extend: 'Ext.app.Controller',
    views: ['Main'],
    models: ['DataModel'],
    stores: ['DataStore'],
    init: function () {
        this.control({
            'Main': {
                render: this.onEditorRender
            }
        });
    },
    onEditorRender: function () {
        console.log("Trade Plan View was rendered");
    }
});
