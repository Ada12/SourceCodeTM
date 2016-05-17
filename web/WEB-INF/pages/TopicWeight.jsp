<%--
  Created by IntelliJ IDEA.
  User: yangchen
  Date: 16-5-2
  Time: 下午8:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title></title>
    <link href="/css/bootstrap.css" rel="stylesheet" type="text/css"/>
    <link href="/css/main.css" rel="stylesheet" type="text/css"/>
    <link href="/css/jquery.range.css" rel="stylesheet" type="text/css"/>

    <style>
        /*.bubble circle{*/
            /*stroke: black;*/
            /*stroke-width: 2px;*/
        /*}*/

        .bubble text{
            fill: black;
            font-size: 14px;
            font-family: arial;
            text-anchor: middle;
        }

        .svg {
            border:1px solid #CCCCCC;
            margin-top: 20px;
            margin-left: 70px;
            /*float: right;*/
        }


    </style>
    <link href="/css/bootstrap.css" rel="stylesheet" type="text/css"/>
    <link href="/css/main.css" rel="stylesheet" type="text/css"/>
    <script src="/js/d3.min.js" charset="utf-8"></script>
    <script src="/js/jquery.min.js" charset="utf-8"></script>
    <script src="/js/bootstrap.min.js" charset="utf-8"></script>
    <script src="/js/jquery.range.js" charset="utf-8"></script>

    <script>

    </script>
</head>
<body>

<div class="controlPos col-lg-6">
    <label for="topicType" class="control-label">Topic Type</label>
    <select class="form-control" id="topicType">
        <option>topics based on tomcat</option>
        <option>topics based on catalina</option>
        <option>topics based on tribes</option>
    </select>
    <br/>
    <label for="topicNum" class="control-label">Topic Number</label>
            <%--<input type="text" class="form-control" id="topicNum" placeholder="Topic Number">--%>
    <br/>
    <div class="mslider">
        <input type="hidden" id="topicNum" class="single-slider" value="45" >
    </div>
    <br/>
    <br/>
</div>

<script>

    function getBubble(){
        var width  = 700;	//SVG绘制区域的宽度950
        var height = 800;	//SVG绘制区域的高度600
        var svg = d3.select("body")			//选择<body>
                .append("svg")			//在<body>中添加<svg>
                .attr("width", width)	//设定<svg>的宽度属性
                .attr("height", height)
                .attr("class","svg col-lg-7")
                .attr("id", "mysvg");

        //border-top:1px solid #000 stroke="blue"

        var pack = d3.layout.pack()
                .size([ width, height ])
                .sort(null)
                .value(function(d){
                    return d.weight;
                })
                .padding(2);

        var url = window.location.href;
        var content = url.split('/');
//        d3.json("/json/topicWeight/" + content[4] + "/" + content[5],function(error, root){

        var type = $("#topicType").val();
        var nt;
        if(type == "topics based on tomcat"){
            nt = 0;
        }else if(type == "topics based on catalina"){
            nt = 1;
        }else{
            nt = 2;
        }
        var number = $("#topicNum").val();

        d3.json("/json/topicWeight/" + nt + "/" + number,function(error, root){

            var nodes = pack.nodes(root);

            console.log(nodes);

            var color = d3.scale.category20c();

            var bubbles = svg.selectAll(".bubble")
                    .data(nodes.filter(function(d) {
                        return !d.children;
                    }))
                    .enter()
                    .append("g")
                    .attr("class","bubble")
                    .attr("onclick",function(d){
                        ///////////////////////////////////////
                        //return "window.location.href= 'http://10.60.42.62:8001/classTopic/"+content[4] + "/" + content[5]+"'";
                        return "window.location.href= 'http://localhost:8080/classTopic/"+ nt + "/" + number +"'";
                        //return  '$("#topicContent").val = d.title';
                    })
                    .attr("onmousemove", function (d) {

                    })
                    .attr("onmouseout", function(d){

                    });

//                .attr("onmouseover", over())
//                .attr("onmouseout", out());

            bubbles.append("circle")
                    .style("fill",function(d,i){
                        return color(i);
                    })
                    .attr("cx",function(d){ return d.x; })
                    .attr("cy",function(d){ return d.y; })
                    .attr("r",function(d){ return d.r; });

            bubbles.append("text")
                    .attr("x",function(d){ return d.x; })
                    .attr("y",function(d){ return d.y; })
                    .text(function(d){
                        return d.name;
                    });


        });
    }

    function geneBubble(){
        var topicType = $("#topicType option:selected").val();
        var topicNum = $("#topicNum").val();
        var type;
        if(topicType == "topics based on tomcat"){
            type = 1;
        }else{
            type = 0;
        }
        ///记得该
        //window.location.href = 'http://10.60.42.62:8001/topicWeight/' + type + '/' + topicNum;
//        window.location.href = 'http://localhost:8080/topicWeight/' + type + '/' + topicNum;
        if ( $("#mysvg").length > 0 ) {
            $("#mysvg").remove();
        }
        getBubble();
        if($("#tablePos").length > 0){
            $("#tablePos").remove();
        }
        topicContent();

    }

    function topicContent(){
        var url = window.location.href;
        var content = url.split('/');
        var type = $("#topicType").val();
        var nt;
        if(type == "topics based on tomcat"){
            nt = 0;
        }else if(type == "topics based on catalina"){
            nt = 1;
        }else{
            nt = 2;
        }
        var number = $("#topicNum").val();
        var urlSend = "/json/topicWeight/" + nt + "/" + number;
        var $tb = $('<div id="tablePos" class="col-lg-10 tablePos">\
                    <table id="mytable" class="table table-bordered">\
                    <tbody id="tbody">\
                    </tbody>\
                    </table>\
                    </div>');
        $(document.body).append($tb);
        $.get(urlSend, {Action:"get",Name:"yc"}, function (response, textStatus) {
            var detail = response.children;
            for(var i = 0; i < detail.length; i ++){
                var $panel = $('<tr>\
                <th>'+detail[i]["name"]+'</th>\
                <td>'+ detail[i]["title"] +'</td>\
                </tr>');
                $panel.appendTo($("#tbody"));
            }
        });

    }

    $(function(){
        $('.single-slider').jRange({
            from: 0,
            to: 100,
            step: 1,
            scale: [0, 20, 40, 60, 80,100],
            format: '%s',
            width: 280,
            showLabels: true,
            showScale: true
        });
    })

    topicNum = document.getElementById("topicNum");
    oldValue = topicNum.value;

    var isChanged = false;

    function track_change(){
        if(topicNum.value != oldValue){
            oldValue = topicNum.value;
            isChanged = true;
//            document.getElementById("testin").innerHTML= topicNum.value ;
//            $("#testin").text(topicNum.value);
            geneBubble();
        }
    }
    setInterval(function() { track_change()}, 100);


    //    function setNum(myValue){
//        if ($('#topicNum').val() != myVaule){
//            $('#topicNum').val(myValue).trigger('change');
//        }
//    }
//
//    $('#topicNum').change(function () {
//        alert("test");
//    })
</script>
</body>
</html>
