Annotator.Plugin.SAConcepts = function (element, options) {
  var plugin = {};
  var concepts;
  plugin.pluginInit = function () {
    console.log("SAConcepts plugin init");
    var ann = this.annotator;
    var opts = {
      type: "GET",
      dataType: "json"
    };
    $.ajax(options.tagsLocation, opts).done(function(data){
      concepts = data;
    });
    this.annotator.editor.addField({
      load: function (field, annotation) {
        console.log("loading editor tags field");
        field.innerHTML="";
        var jqfield = $(field);
        var container = $("<div>");
        container.attr("id", "tagContainer");
        container.attr("style", "width:350px");
        jqfield.append(container);
        var select = $("<select>");
        select.attr("id", "tagSelector");
        select.attr("class", "form-control");
        //select.attr("data-placeholder","Select a tag...");
        //select.attr("multiple", "");
        //select.attr('tabindex','7');
        //select.attr("style","width: 350px;");
        for(var i in concepts){
          var group = $("<optgroup>");
          group.attr("label", concepts[i].name);
          group.attr("style", "font-weight: 600; color: rgb(115, 115, 115);")
          for (var j = 0; j < concepts[i].tags.length; j++) {
            var opt  = $("<option>");
            opt.append(concepts[i].tags[j]);
            if(annotation.tag != undefined){
              var tag = annotation.tag;
              if(tag == concepts[i].tags[j]){
                opt.attr("selected", "");
              }
            }
            group.append(opt);
          }
          select.append(group);
        }
        container.append(select);
        /*$("#tagSelector").chosen({max_selected_options: 1});
        $("#tagSelector").chosen().change(function(eve, obj){
          if(obj.deselected != undefined && obj.deselected.indexOf("Tactic") != -1){
            annotation.tactic = undefined;  
          }
        });*/
      }
    });

this.annotator.viewer.addField({
  load: function(field, annotation){ 
    console.log("loading viewer tags field");           
    field.innerHTML = "";
    var x = $("<div>");
    x.attr("class", "annotator-tags");
    var t = $("<span>");
    t.attr("class", "annotator-tag");
    t.append(annotation.tag);
    x.append(t);
    if(annotation.tag != undefined && annotation.tag.indexOf("Tactic") != -1 && annotation.tactic != undefined){
      var tac = $("<span>");
      tac.attr("class", "annotator-tag");
      tac.append(annotation.tactic);
      x.append(tac);
    }
    $(field).append(x);
  }
}).addField({
  load: function(field, annotation){
    console.log("loading viewer fragment manager field");
    if(annotation.tag != undefined){
      var a = $("<a>");
      //a.attr("href", "/templateEditor/" + options.docId + "/" + annotation.id);
      a.attr("target", "_parent");
      a.append("Add/Remove from Structured Representation");
      a.attr("data-toggle","modal");
      a.attr("data-target","#syntax");
      a.click(function(){
        ann.viewer.hide();
        //var modal = parent.document.getElementById("syntax");
        //$(modal).modal({ keyboard: true});
       //$(modal).modal('toggle');
        $(parent.document.getElementById("elements")).load("/templateEditor/"+options.docId+"/"+annotation.id);
      });
      $(field).append(a);
    }
  }
});

this.annotator.subscribe("annotationEditorShown", function(editor, annotation){
  var extra1 = $(".chosen-container > .annotator-controls");
  while(extra1.length > 0){
    extra1[0].remove();
    extra1 = $(".chosen-container > .annotator-controls");
  }

  var extra2 = $(".chosen-drop > .annotator-controls");
  while(extra2.length > 0){
    extra2[0].remove();
    extra2 = $(".chosen-container > .annotator-controls");
  }
}).subscribe("annotationEditorSubmit", function(editor, annotation){
  var tag = document.getElementById("tagSelector").value;

  //$("div#tagContainer > div.chosen-container > ul.chosen-choices > li.search-choice > span").html();

  annotation.tag = tag;
}).subscribe("annotationsLoaded", function(annotations){
  console.log("annotations loaded");
});

}
return plugin;
}	