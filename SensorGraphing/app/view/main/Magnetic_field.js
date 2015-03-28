Ext.define('MyApp.view.main.Magnetic_field', {
	
	
    extend: 'Ext.container.Container',
	
    requires: [
        'MyApp.view.main.MainController',
        'MyApp.view.main.MainModel'
    ],
	alias: 'widget.Main',
    xtype: 'app-main',
	
	var: model = Ext.define('User', {
		extend: 'Ext.data.Model',
		fields: ['Device', 'timevalue', 'Magnetic_x', 'Magnetic_y', 'Magnetic_x']
	}),

	var: myStore = Ext.create('Ext.data.Store', {
		model: model,
		proxy: {
			type: 'ajax',
			url: '/duomenys/Magnetic_field.txt',
			reader: {
				type: 'json'
			}
		},
		autoLoad: true
	}),
	
	
   
    controller: 'main',
    viewModel: {
        type: 'main'
    },

    layout: {
        type: 'border'
    },

    items: [
	{
	region: 'north',
	xtype : 'component',
	padding : 10,
	height : 40,		
        html : '<font size="5" color="white"><b>Sensors data.</b></font'
	},{
        region: 'center',
        xtype: 'tabpanel',
        items:[{
			title: 'Magnetic field values',
			layout: 'fit',
				items: [{
					xtype: 'chart',
					store: myStore,
					interactions: ['panzoom'],
						
					legend: {
						docked: 'bottom'
					},
					//define x and y axis.
					axes: [
						{
							type: 'numeric',
							position: 'left'
						},
						{
							type: 'category',
							visibleRange: [0, 1],
							position: 'bottom'
						}
					],
					//define the actual series
					series: [{
						type: 'line',
						xField: 'timevalue',
						yField: 'Magnetic_x',
						title: 'Magnetic field x',
						style: {
							//smooth: true,
							//fill: "#94ae0a",
							stroke: "#94ae0a",
							fillOpacity: 0.6,
							miterLimit: 3,
							lineCap: 'miter',
							lineWidth: 4
						}
					},{
						type: 'line',
						xField: 'timevalue',
						yField: 'Magnetic_y',
						title: 'Magnetic field y',
						style: {
							//smooth: true,
							//fill: "#0528ed",
							stroke: "#0528ed",
							fillOpacity: 0.6,
							miterLimit: 3,
							lineCap: 'miter',
							lineWidth: 4
						}
					},{
						type: 'line',
						xField: 'timevalue',
						yField: 'Magnetic_z',
						title: 'Magnetic field z',
						style: {
							//smooth: true,
							//fill: "#edca05",
							stroke: "#edca05",
							fillOpacity: 0.6,
							miterLimit: 3,
							lineCap: 'miter',
							lineWidth: 4
						}
					}]
				}]
		},{
			title: 'Data',
			layout: 'fit',
			items: [{
				xtype: 'grid',
				store: myStore, 
				columns: [
					{ text: 'Device',  dataIndex: 'Device', flex: 1 },
					{ text: 'Time', dataIndex: 'timevalue', flex: 1.5 },
					{ text: 'Magnetic field x', dataIndex: 'Magnetic_x', flex: 1 },
					{ text: 'Magnetic field y', dataIndex: 'Magnetic_y', flex: 1 },
					{ text: 'Magnetic field z', dataIndex: 'Magnetic_z', flex: 1 }
				]
			}]
		}]
    }]
});
