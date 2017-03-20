// drawing graph helper methods
var acuteline = d3.line()
	.x(function(d) { return xfocus(d.SESS_START); })
	.y(function(d) { return yfocus(d.ACUTE); });

function acuteline2(h) {
	
	return d3.line()
	.x(function(d) { return xfocus(d.SESS_START); })
	.y(h);
}
	
	

function chronicloadarea(h) {
	return d3.area()
	.curve(d3.curveMonotoneX)
	.x(function(d) { return xfocus(d.SESS_START); })
	.y0(h)
	.y1(function(d) { return yfocus(d.CHRONIC); });
}

function chronicloadarea2(h) {
	return d3.area()
	.curve(d3.curveMonotoneX)
	.x(function(d) { return xfocus(d.SESS_START); })
	.y0(h)
	.y1(h);
}




var sessloadline = d3.line()
.curve(d3.curveMonotoneX)
.defined(function(d,i) { return i != 0;})
.x(function(d) { 
	var midpoint = new Date((d.SESS_END.getTime() + d.SESS_START.getTime()) /2);
	return xfocus(midpoint); })
.y(function(d) { return yfocus(d.SESS_LOAD + d.GAME_LOAD); });

function sessloadline2(h) {
	return d3.line()
	.curve(d3.curveMonotoneX)
	.defined(function(d,i) { return i != 0;})
	.x(function(d) { return xfocus(d.SESS_START); })
	.y(h);
}



