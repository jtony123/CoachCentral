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


//the cod's
var gh = 800;

var cod_l_lo_area = d3.area()
.x(function(d) { return xfocus(d.SESS_START); })
.y0(function(d) { return yfocus(gh); })
.y1(function(d) { return yfocus(gh - d.COD_L_LO); });

var cod_l_lo_area2 = d3.area()
.x(function(d) { return xfocus(d.SESS_START); })
.y0(function(d) { return yfocus(gh); })
.y1(function(d) { return yfocus(gh); });

var cod_l_med_area = d3.area()
.x(function(d) { return xfocus(d.SESS_START); })
.y0(function(d) { return yfocus(gh - d.COD_L_LO); })
.y1(function(d) { return yfocus(gh - (d.COD_L_LO + d.COD_L_MED)); });

var cod_l_med_area2 = d3.area()
.x(function(d) { return xfocus(d.SESS_START); })
.y0(function(d) { return yfocus(gh); })
.y1(function(d) { return yfocus(gh); });

var cod_l_hi_area = d3.area()
.x(function(d) { return xfocus(d.SESS_START); })
.y0(function(d) { return yfocus(gh - (d.COD_L_LO + d.COD_L_MED)); })
.y1(function(d) { return yfocus(gh - (d.COD_L_LO + d.COD_L_MED + d.COD_L_HI)); });

var cod_l_hi_area2 = d3.area()
.x(function(d) { return xfocus(d.SESS_START); })
.y0(function(d) { return yfocus(gh); })
.y1(function(d) { return yfocus(gh); });

var cod_r_lo_area = d3.area()
.x(function(d) { return xfocus(d.SESS_START); })
.y0(function(d) { return yfocus(gh); })
.y1(function(d) { return yfocus(gh + d.COD_R_LO); });

var cod_r_lo_area2 = d3.area()
.x(function(d) { return xfocus(d.SESS_START); })
.y0(function(d) { return yfocus(gh); })
.y1(function(d) { return yfocus(gh); });

var cod_r_med_area = d3.area()
.x(function(d) { return xfocus(d.SESS_START); })
.y0(function(d) { return yfocus(gh + d.COD_R_LO); })
.y1(function(d) { return yfocus(gh + (d.COD_R_LO  + d.COD_R_MED)); });

var cod_r_med_area2 = d3.area()
.x(function(d) { return xfocus(d.SESS_START); })
.y0(function(d) { return yfocus(gh); })
.y1(function(d) { return yfocus(gh); });

var cod_r_hi_area = d3.area()
.x(function(d) { return xfocus(d.SESS_START); })
.y0(function(d) { return yfocus(gh + (d.COD_R_LO + d.COD_R_MED)); })
.y1(function(d) { return yfocus(gh + (d.COD_R_LO + d.COD_R_MED + d.COD_R_HI)); });

var cod_r_hi_area2 = d3.area()
.x(function(d) { return xfocus(d.SESS_START); })
.y0(function(d) { return yfocus(gh); })
.y1(function(d) { return yfocus(gh); });

//the jumps
var jump_lo_area = d3.area()
.curve(d3.curveMonotoneX)
.x(function(d) { return xfocus(d.SESS_START); })
.y0(function(d) { return yfocus(0); })
.y1(function(d) { return yfocus(d.JUMP_LO); });

var jump_lo_area2 = d3.area()
.curve(d3.curveMonotoneX)
.x(function(d) { return xfocus(d.SESS_START); })
.y0(function(d) { return yfocus(0); })
.y1(function(d) { return yfocus(0); });

var jump_med_area = d3.area()
.curve(d3.curveMonotoneX)
.x(function(d) { return xfocus(d.SESS_START); })
.y0(function(d) { return yfocus(d.JUMP_LO); })
.y1(function(d) { return yfocus(d.JUMP_LO + d.JUMP_MED); });

var jump_med_area2 = d3.area()
.curve(d3.curveMonotoneX)
.x(function(d) { return xfocus(d.SESS_START); })
.y0(function(d) { return yfocus(0); })
.y1(function(d) { return yfocus(0); });

var jump_hi_area = d3.area()
.curve(d3.curveMonotoneX)
.x(function(d) { return xfocus(d.SESS_START); })
.y0(function(d) { return yfocus(d.JUMP_LO + d.JUMP_MED); })
.y1(function(d) { return yfocus(d.JUMP_LO + d.JUMP_MED + d.JUMP_HI); });

var jump_hi_area2 = d3.area()
.curve(d3.curveMonotoneX)
.x(function(d) { return xfocus(d.SESS_START); })
.y0(function(d) { return yfocus(0); })
.y1(function(d) { return yfocus(0); });

//the heart rate line
var heartrateline = d3.line()
.curve(d3.curveMonotoneX)
.x(function(d) { return xfocus(d.SESS_START); })
.y(function(d) { return yfocus(d.HR_EXERT); });

var heartrateline2 = d3.line()
.curve(d3.curveMonotoneX)
.x(function(d) { return xfocus(d.SESS_START); })
.y(function(d) { return yfocus(0); });

var accelline = d3.line()
.x(function(d) { return xfocus(d.SESS_START); })
.y(function(d) { return yfocus(d.ACCEL_TOTAL); });

var accelline2 = d3.line()
.x(function(d) { return xfocus(d.SESS_START); })
.y(function(d) { return yfocus(0); });

var decelline = d3.line()
.x(function(d) { return xfocus(d.SESS_START); })
.y(function(d) { return yfocus(d.DECEL_TOTAL); });

var decelline2 = d3.line()
.x(function(d) { return xfocus(d.SESS_START); })
.y(function(d) { return yfocus(0); });


var amberzone = 0.1;


var defenceline = d3.line()
.curve(d3.curveMonotoneX)
.x(function(d) { return xfocus(d.TEST_TIME); })
.y(function(d) { return yfocus2(d.FORD); });

var defenceadjline = d3.line()
.curve(d3.curveMonotoneX)
.defined(function(d,i) { return i != 0;})
.x(function(d) { return xfocus(d.TEST_TIME); })
.y(function(d) { return yfocus2(d.FORD_ADJ); });

var defenceadjline2 = d3.line()
.curve(d3.curveMonotoneX)
.defined(function(d,i) { return i != 0;})
.x(function(d) { return xfocus(d.TEST_TIME); })
.y(function(d) { return yfocus2(d.FORD_ADJ2); });

var defenceamberarea = d3.area()
.curve(d3.curveMonotoneX)
.defined(function(d,i) { return i != 0;})
.x(function(d) { return xfocus(d.TEST_TIME); })
.y0(function(d) { 
	var tenpercent = (d.FORT_ADJ - d.FORD_ADJ) * amberzone;
	return yfocus2(d.FORD_ADJ + tenpercent); })
.y1(function(d) { return yfocus2(d.FORD_ADJ); });

var defenceamberarea2 = d3.area()
.curve(d3.curveMonotoneX)
.defined(function(d,i) { return i != 0;})
.x(function(d) { return xfocus(d.TEST_TIME); })
.y0(function(d) { 
	var tenpercent = (d.FORT_ADJ2 - d.FORD_ADJ2) * amberzone;
	return yfocus2(d.FORD_ADJ2 + tenpercent); })
.y1(function(d) { return yfocus2(d.FORD_ADJ2); });

var safearea = d3.area()
.curve(d3.curveMonotoneX)
.defined(function(d,i) { return i != 0;})
.x(function(d) { return xfocus(d.TEST_TIME); })
.y0(function(d) { 
	var tenpercent = (d.FORT_ADJ - d.FORD_ADJ) * amberzone;
	return yfocus2(d.FORD_ADJ + tenpercent); })
.y1(function(d) { 
	var tenpercent = (d.FORT_ADJ - d.FORD_ADJ) * amberzone;
	return yfocus2(d.FORT_ADJ - tenpercent); });

var safearea2 = d3.area()
.curve(d3.curveMonotoneX)
.defined(function(d,i) { return i != 0;})
.x(function(d) { return xfocus(d.TEST_TIME); })
.y0(function(d) { 
	var tenpercent = (d.FORT_ADJ2 - d.FORD_ADJ2) * amberzone;
	return yfocus2(d.FORD_ADJ2 + tenpercent); })
.y1(function(d) { 
	var tenpercent = (d.FORT_ADJ2 - d.FORD_ADJ2) * amberzone;
	return yfocus2(d.FORT_ADJ2 - tenpercent); });

function dangerarea(h) {
	return d3.area()
	.curve(d3.curveMonotoneX)
	.defined(function(d,i) { return i != 0;})
	.x(function(d) { return xfocus(d.TEST_TIME); })
	.y0(h)
	.y1(0);
}

function dangerarea2(h) {
	return d3.area()
	.curve(d3.curveMonotoneX)
	.defined(function(d,i) { return i != 0;})
	.x(function(d) { return xfocus(d.TEST_TIME); })
	.y0(h)
	.y1(0);
}




var stressline = d3.line()
.curve(d3.curveMonotoneX)
.x(function(d) { return xfocus(d.TEST_TIME); })
.y(function(d) { return yfocus2(d.FORT); });

var stressadjline = d3.line()
.curve(d3.curveMonotoneX)
.defined(function(d,i) { return i != 0;})
.x(function(d) { return xfocus(d.TEST_TIME); })
.y(function(d) { return yfocus2(d.FORT_ADJ); });

var stressadjline2 = d3.line()
.curve(d3.curveMonotoneX)
.defined(function(d,i) { return i != 0;})
.x(function(d) { return xfocus(d.TEST_TIME); })
.y(function(d) { return yfocus2(d.FORT_ADJ2); });

var stressamberarea = d3.area()
.curve(d3.curveMonotoneX)
.defined(function(d,i) { return i != 0;})
.x(function(d) { return xfocus(d.TEST_TIME); })
.y1(function(d) { 
	var tenpercent = (d.FORT_ADJ - d.FORD_ADJ) * amberzone;
	return yfocus2(d.FORT_ADJ - tenpercent); })
.y0(function(d) { return yfocus2(d.FORT_ADJ); });

var stressamberarea2 = d3.area()
.curve(d3.curveMonotoneX)
.defined(function(d,i) { return i != 0;})
.x(function(d) { return xfocus(d.TEST_TIME); })
.y1(function(d) { 
	var tenpercent = (d.FORT_ADJ2 - d.FORD_ADJ2) * amberzone;
	return yfocus2(d.FORT_ADJ2 - tenpercent); })
.y0(function(d) { return yfocus2(d.FORT_ADJ2); });

//var vertlines = d3.line()
//.x1(function(d) { return xfocus(d.TEST_TIME); })
//.y1(0)
//.x2(function(d) { return xfocus(d.TEST_TIME); })
//.y2(300);




