<%--
  Created by IntelliJ IDEA.
  User: yangchen
  Date: 16-4-17
  Time: 下午2:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <script language="JavaScript" src="/js/jquery.min.js"></script>
    <script type="text/javascript">
        function getResult(){
            var url = window.location.href;
            var content = url.split('/');
            var urlSend = "/json/rtc/";
            $.get(urlSend, {Action:"get",Name:"yc"}, function (response, textStatus){
                $('#container').highcharts({
                    colors: ["#2b908f", "#90ee7e", "#f45b5b", "#7798BF", "#aaeeee", "#ff0066", "#eeaaee", "#55BF3B", "#DF5353", "#7798BF", "#aaeeee"],
                    chart: { backgroundColor: { linearGradient: { x1: 0, y1: 0, x2: 1, y2: 1 }, stops: [ [0, '#2a2a2b'], [1, '#3e3e40'] ] }, style: { fontFamily: "'Unica One', sans-serif" }, plotBorderColor: '#606063' },
                    title: {
                        style: { color: '#E0E0E3', textTransform: 'uppercase', fontSize: '20px' },
                        text: "The RTCs",
                        x: -20 //center
                    },
                    xAxis: {
                        gridLineColor: '#707073',
                        labels: {
                            style: {
                                color: '#E0E0E3'
                            }
                        },
                        lineColor: '#707073',
                        minorGridLineColor: '#505053',
                        tickColor: '#707073',
                        title: { style: { color: '#A0A0A3' } },
                        categories: ['10','20', '30', '40', '50', '60', '70', '80','90', '100']
                    },
                    yAxis: {
                        gridLineColor: '#707073', labels: { style: { color: '#E0E0E3' } }, lineColor: '#707073', minorGridLineColor: '#505053', tickColor: '#707073', tickWidth: 1, title: { style: { color: '#A0A0A3' } },
                        title: {
                            text: "rate"
                        },
                        plotLines: [{
                            value: 0,
                            width: 1,
                            color: '#808080'
                        }]
                    },
                    tooltip: {
                        backgroundColor: 'rgba(0, 0, 0, 0.85)', style: { color: '#F0F0F0' },
                        valueSuffix: "%"
                    },
                    legend: {
                        itemStyle: { color: '#E0E0E3' }, itemHoverStyle: { color: '#FFF' }, itemHiddenStyle: { color: '#606063' },
                        layout: 'vertical',
                        align: 'right',
                        verticalAlign: 'middle',
                        borderWidth: 0
                    }
                });
                var chart = $('#container').highcharts();
                var rates = response.result["rates"];
                var rate = rates.split(",");
                var data = [];
                for(var j = 0; j < rate.length; j ++){
                    if(rate[j].toString() == ""){
                        data[j] = 0;
                    }else{
                        data[j] = parseFloat(rate[j]);
                    }
                }
                chart.addSeries({
                    data: data
                });
            })
        }

        $(document).ready(function(){
            $('#highcharts-0').click();
        });

    </script>
</head>
<body style="background-color: black" onload="getResult()">
<script language="JavaScript" src="/js/chart/highcharts.js"></script>
<script language="JavaScript" src="/js/chart/exporting.js"></script>
<!--<div id="container" style="min-width: 310px; height: 400px; margin: 0 auto"></div>-->
<div id="container" style="height: 537px; margin: 0px auto"></div>
</body>
</html>

