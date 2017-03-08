// drawing graph helper methods
var acuteline = d3.line()
	.x(function(d) { return xfocus(d.SESS_START); })
	.y(function(d) { return yfocus(d.ACUTE); });
	
	
function chronicloadarea(h) {
	return d3.area()
	.curve(d3.curveMonotoneX)
	.x(function(d) { return xfocus(d.SESS_START); })
	.y0(h)
	.y1(function(d) { return yfocus(d.CHRONIC); });
}

var sessloadline = d3.line()
.curve(d3.curveMonotoneX)
.defined(function(d,i) { return i != 0;})
.x(function(d) { return xfocus(d.SESS_START); })
.y(function(d) { return yfocus(d.SESS_LOAD + d.GAME_LOAD); });

//the cod's
var gh = 800;

var cod_l_lo_area = d3.area()
.x(function(d) { return xfocus(d.SESS_START); })
.y0(function(d) { return yfocus(gh); })
.y1(function(d) { return yfocus(gh - d.COD_L_LO); });

var cod_l_med_area = d3.area()
.x(function(d) { return xfocus(d.SESS_START); })
.y0(function(d) { return yfocus(gh - d.COD_L_LO); })
.y1(function(d) { return yfocus(gh - (d.COD_L_LO + d.COD_L_MED)); });

var cod_l_hi_area = d3.area()
.x(function(d) { return xfocus(d.SESS_START); })
.y0(function(d) { return yfocus(gh - (d.COD_L_LO + d.COD_L_MED)); })
.y1(function(d) { return yfocus(gh - (d.COD_L_LO + d.COD_L_MED + d.COD_L_HI)); });

var cod_r_lo_area = d3.area()
.x(function(d) { return xfocus(d.SESS_START); })
.y0(function(d) { return yfocus(gh); })
.y1(function(d) { return yfocus(gh + d.COD_R_LO); });

var cod_r_med_area = d3.area()
.x(function(d) { return xfocus(d.SESS_START); })
.y0(function(d) { return yfocus(gh + d.COD_R_LO); })
.y1(function(d) { return yfocus(gh + (d.COD_R_LO  + d.COD_R_MED)); });

var cod_r_hi_area = d3.area()
.x(function(d) { return xfocus(d.SESS_START); })
.y0(function(d) { return yfocus(gh + (d.COD_R_LO + d.COD_R_MED)); })
.y1(function(d) { return yfocus(gh + (d.COD_R_LO + d.COD_R_MED + d.COD_R_HI)); });

//the jumps
var jump_lo_area = d3.area()
.curve(d3.curveMonotoneX)
.x(function(d) { return xfocus(d.SESS_START); })
.y0(function(d) { return yfocus(0); })
.y1(function(d) { return yfocus(d.JUMP_LO); });

var jump_med_area = d3.area()
.curve(d3.curveMonotoneX)
.x(function(d) { return xfocus(d.SESS_START); })
.y0(function(d) { return yfocus(d.JUMP_LO); })
.y1(function(d) { return yfocus(d.JUMP_LO + d.JUMP_MED); });

var jump_hi_area = d3.area()
.curve(d3.curveMonotoneX)
.x(function(d) { return xfocus(d.SESS_START); })
.y0(function(d) { return yfocus(d.JUMP_LO + d.JUMP_MED); })
.y1(function(d) { return yfocus(d.JUMP_LO + d.JUMP_MED + d.JUMP_HI); });

//the heart rate line
var heartrateline = d3.line()
.curve(d3.curveMonotoneX)
.x(function(d) { return xfocus(d.SESS_START); })
.y(function(d) { return yfocus(d.HR_EXERT); });

var accelline = d3.line()
.x(function(d) { return xfocus(d.SESS_START); })
.y(function(d) { return yfocus(d.ACCEL_TOTAL); });

var decelline = d3.line()
.x(function(d) { return xfocus(d.SESS_START); })
.y(function(d) { return yfocus(d.DECEL_TOTAL); });

var defenceline = d3.line()
.curve(d3.curveMonotoneX)
.x(function(d) { return xfocus(d.TEST_TIME); })
.y(function(d) { return yfocus2(d.FORD); });

var defenceadjline = d3.line()
.curve(d3.curveMonotoneX)
.defined(function(d,i) { return i != 0;})
.x(function(d) { return xfocus(d.TEST_TIME); })
.y(function(d) { return yfocus2(d.FORD_ADJ); });

var stressline = d3.line()
.curve(d3.curveMonotoneX)
.x(function(d) { return xfocus(d.TEST_TIME); })
.y(function(d) { return yfocus2(d.FORT); });

var stressadjline = d3.line()
.curve(d3.curveMonotoneX)
.defined(function(d,i) { return i != 0;})
.x(function(d) { return xfocus(d.TEST_TIME); })
.y(function(d) { return yfocus2(d.FORT_ADJ); });
