<%--
  Created by IntelliJ IDEA.
  User: yangchen
  Date: 16-5-7
  Time: 下午10:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <link href="/css/bootstrap.css" rel="stylesheet" type="text/css"/>
    <link href="/css/main.css" rel="stylesheet" type="text/css"/>
    <script language="JavaScript" src="/js/jquery.min.js"></script>
    <script src="/js/bootstrap.min.js" charset="utf-8"></script>

        </head>
<body style="background-color: #ffffff" onload="getResult(), geneTable()">
<script language="JavaScript" src="/js/chart/highcharts.js"></script>
<script language="JavaScript" src="/js/chart/exporting.js"></script>
<div id="container" style="height: 537px; margin: 0px auto"></div>
<div class="col-lg-12">
    <div class="col-lg-4"></div>
    <div class="col-lg-1"><a href="http://localhost:8080/topicWeight"><span class="glyphicon glyphicon-arrow-left myTh back"></span></a></div>
    <div class="col-lg-2"></div>
    <div class="col-lg-1"><a id="go"><span class="glyphicon glyphicon-arrow-right myTh back"></span></a></div>
    <div class="col-lg-4"></div>
</div>
<div id="tablePos" class="col-lg-10 tablePos"></div>
<div id="highTopicTable" class="col-lg-10 tablePos"></div>
<div id="lowTopicTable" class="col-lg-10 tablePos"></div>
<script type="text/javascript">
    function getResult(){
        var url = window.location.href;
        var content = url.split('/');
        var urlSend = "/json/stackMWE/" + content[4] + "/" + content[5] + "/" + content[6];
        categories = [];
        $.get(urlSend, {Action:"get",Name:"yc"}, function (response, textStatus) {
            $('#container').highcharts({
                chart: {
                    type: 'column'
                },

                title: {
                    text: content[6]
                },

//                    xAxis: {
//                        categories: ['Apples', 'Oranges', 'Pears', 'Grapes', 'Bananas']
//                    },

                yAxis: {
//                        allowDecimals: false,
//                        min: 0,
                    title: {
                        text: '%'
                    }
                },

                tooltip: {
                    formatter: function () {
                        return '<b>' + this.x + '</b><br/>' +
                                this.series.name + ': ' + this.y + '<br/>' +
                                'Total: ' + this.point.stackTotal;
                    }
                },

                plotOptions: {
                    column: {
                        stacking: 'normal'
                    },
//                        cursor: 'pointer',
//                        events: {
//                            click: function(event) {
//                                alert(this.name +' clicked\n'+'highcharts 交流群294191384');
//                            }
//                        }
                    series: {
                        cursor: 'pointer',
                        events: {
                            click: function(e) {
//                                alert(e.point.category);
                                geneTopicContent(e.point.category);
                            }
                        }
                    }
                }
            });

            var chart = $('#container').highcharts();
            for(var i=0; i<content[4]; i++){
                categories[i] = i;
            }
            chart.xAxis[0].setCategories(categories);
            var high = response.result["high"];
            var h = high.split("-");
            var hdata = [];
            for(var j = 0; j < h.length-1; j ++){
                if(h[j].toString() == ""){
                    hdata[j] = 0;
                }else{
                    hdata[j] = parseFloat(h[j]);
                }
            }
            chart.addSeries({
                name: 'high',
                data: hdata,
                type: 'column'
            });

            var center = response.result["center"];
            var c = center.split("-");
            var cdata = [];
            for(var k = 0; k < c.length-1; k ++){
                if(c[k].toString() == ""){
                    cdata[c] = 0;
                }else{
                    cdata[k] = parseFloat(c[k]);
                }
            }
            chart.addSeries({
                name: 'center',
                data: cdata,
                type: 'column'
            });

            var low = response.result["low"];
            var l = low.split("-");
            var ldata = [];
            for(var m = 0; m < l.length-1; m ++){
                if(l[m].toString() == ""){
                    ldata[m] = 0;
                }else{
                    ldata[m] = parseFloat(l[m]);
                }
            }
            chart.addSeries({
                name: 'low',
                data: ldata,
                type: 'column'
            });

            var rates = response.result["rates"];
            var rate = rates.split(",");
            var data = [];
            for(var j = 0; j < rate.length-1; j ++){
                if(rate[j].toString() == ""){
                    data[j] = 0;
                }else{
                    data[j] = parseFloat(rate[j]);
                }
            }
            chart.addSeries({
                name: 'MWE',
                data: data,
                type: 'spline'
            })
        })
    }
    $(document).ready(function(){
        $('#highcharts-0').click();
    });

    function geneTopicContent(id){
        if($("#mytable").length > 0){
            $("#mytable").remove();
        }
        geneContent(id);
    }

    function geneTable(){
        var url = window.location.href;
        var content = url.split('/');
        var urlSend = "/json/higherMWE/" + content[4] + "/" + content[5];
        var $tb = $('<table class="table table-bordered">\
                    <tbody id="bodytr">\
                    </tbody>\
                    </table>');
        $tb.appendTo($("#highTopicTable"));

        var $ntb = $('<table class="table table-bordered">\
                    <tbody id="nbodytr">\
                    </tbody>\
                    </table>');
        $ntb.appendTo($("#lowTopicTable"));
        $.get(urlSend, {Action: "get", Name: "yc"}, function (response, textStatus) {
            var detail = response.result;

            var nn = Math.floor(detail.length/3);
            for(var a = 0; a < nn; a ++){
                var $ntr1 = $('<tr id="nmytr1'+ a +'"></tr>');
                var $ntr2 = $('<tr id="nmytr2'+ a +'"></tr>');
                $ntr1.appendTo($("#nbodytr"));
                $ntr2.appendTo($("#nbodytr"));
                for(var i=3*a; i < 3*(a+1); i++){
                    var $nmthead = $('<th class="myTh"><a href="http://localhost:8080/stackMWE/'+ content[4]+ '/' +content[5] +'/'+ detail[i]["module"] +'">'+ detail[i]["module"] +'</a></th>');
                    $nmthead.appendTo($("#nmytr1"+a));
                    var $nmth = $('<td></td>');
                    for(var j = 0; j < detail[i]["end"].length; j++){
                        var $nmsp = $('<span style="background-color: '+ detail[i]["end"][j]["color"] +'; color: #FFFFFF">'+ detail[i]["end"][j]["topic"] +'</span>&nbsp;');
                        var $ncomma = $('<span>,</span>');
                        $nmsp.appendTo($nmth);
                        $ncomma.appendTo($nmth);
                    }
//                    var $mth = $('<td>'+ detail[i]["begin"] +'</td>');
                    $nmth.appendTo($("#nmytr2"+a));
                }
            }
            var $ntr1 = $('<tr id="nmytr1end"></tr>');
            var $ntr1 = $('<tr id="nmytr2end"></tr>');
            $ntr1.appendTo($("#nbodytr"));
            $ntr2.appendTo($("#nbodytr"));
            for(var i=n*3; i < detail.length; i++){
                var $nmthead = $('<th class="myTh"><a href="http://localhost:8080/stackMWE/'+ content[4]+ '/' +content[5] +'/'+ detail[i]["module"] +'">'+ detail[i]["module"] +'</a></th>');
                $nmthead.appendTo($("#nmytr1end"));
                var $nmth = $('<td></td>');
                for(var j = 0; j < detail[i]["end"].length; j++){
                    var $nmsp = $('<span style="background-color: '+ detail[i]["end"][j]["color"] +'; color: #FFFFFF">'+ detail[i]["end"][j]["topic"] +'</span>&nbsp;');
                    var $ncomma = $('<span>,</span>');
                    $nmsp.appendTo($nmth);
                    $ncomma.appendTo($nmth);
                }
//                var $mth = $('<td>'+ detail[i]["begin"] +'</td>');
                $nmth.appendTo($("#nmytr2end"));
            }


            var n = Math.floor(detail.length/9);
            for(var a = 0; a < n; a ++){
                var $tr1 = $('<tr id="mytr1'+ a +'"></tr>');
                var $tr2 = $('<tr id="mytr2'+ a +'"></tr>');
                $tr1.appendTo($("#bodytr"));
                $tr2.appendTo($("#bodytr"));
                for(var i=9*a; i < 9*(a+1); i++){
                    var $mthead = $('<th class="myTh"><a href="http://localhost:8080/stackMWE/'+ content[4]+ '/' +content[5] +'/'+ detail[i]["module"] +'">'+ detail[i]["module"] +'</a></th>');
                    $mthead.appendTo($("#mytr1"+a));
                    var $mth = $('<td></td>');
                    for(var j = 0; j < detail[i]["begin"].length; j++){
                        var $msp = $('<span style="background-color: '+ detail[i]["begin"][j]["color"] +'">'+ detail[i]["begin"][j]["topic"] +'</span>&nbsp;');
                        var $comma = $('<span>,</span>');
                        $msp.appendTo($mth);
                        $comma.appendTo($mth);
                    }
//                    var $mth = $('<td>'+ detail[i]["begin"] +'</td>');
                    $mth.appendTo($("#mytr2"+a));
                }
            }
            var $tr1 = $('<tr id="mytr1end"></tr>');
            var $tr1 = $('<tr id="mytr2end"></tr>');
            $tr1.appendTo($("#bodytr"));
            $tr2.appendTo($("#bodytr"));
            for(var i=n*9; i < detail.length; i++){
                var $mthead = $('<th class="myTh"><a href="http://localhost:8080/stackMWE/'+ content[4]+ '/' +content[5] +'/'+ detail[i]["module"] +'">'+ detail[i]["module"] +'</a></th>');
                $mthead.appendTo($("#mytr1end"));
                var $mth = $('<td></td>');
                for(var j = 0; j < detail[i]["begin"].length; j++){
                    var $msp = $('<span style="background-color: '+ detail[i]["begin"][j]["color"] +'">'+ detail[i]["begin"][j]["topic"] +'</span>&nbsp;');
                    var $comma = $('<span>,</span>');
                    $msp.appendTo($mth);
                    $comma.appendTo($mth);
                }
//                var $mth = $('<td>'+ detail[i]["begin"] +'</td>');
                $mth.appendTo($("#mytr2end"));
            }

        });
        document.getElementById("go").setAttribute("href", "http://localhost:8080/classTopic/" + content[4] + "/" + content[5])
    }

    function geneContent(id) {
        var url = window.location.href;
        var content = url.split('/');
        var urlSend = "/json/topicWeight/" + content[4] + "/" + content[5] + "/" + id;
        var $tb = $('<table id="mytable" class="table table-bordered">\
                    <tbody id="tbody">\
                    </tbody>\
                    </table>');
//        $(document.body).append($tb);
        $tb.appendTo($("#tablePos"));
        $.get(urlSend, {Action: "get", Name: "yc"}, function (response, textStatus) {
            var detail = response.result;
            var $panel = $('<tr>\
                <th class="myTh">'+id+'</th>\
                <td>'+ detail["title"] +'</td>\
                </tr>');
            $panel.appendTo($("#tbody"));
        })
    }
</script>
</body>
</html>
