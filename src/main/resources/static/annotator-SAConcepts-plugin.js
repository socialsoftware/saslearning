var concepts = ["Scenario", "Availability", "Scalability", "Performance", "Tactic", "Component", "Module"];
       
Annotator.Plugin.SAConcepts = function (element) {
  var plugin = {};
  plugin.pluginInit = function () {
  	this.annotator.editor.addField({
      load: function (field, annotation) {
        field.innerHTML="";
        var jqfield = $(field);
        var container = $("<div>");
        container.attr("id", "tagContainer");
        container.attr("style", "width:350px");
        jqfield.append(container);
        var select = $("<select>");
        select.attr("id", "tagSelector");
        select.attr("data-placeholder","Tags");
        select.attr("multiple", "");
        select.attr("class", "chosen-select");
        select.attr("tabindex","7");
        select.attr("style","width: 350px;");
        for (var i = 0; i < concepts.length; i++) {
          var opt  = $("<option>");
          opt.append(concepts[i]);
          if(annotation.tags != undefined){
            var index = annotation.tags.indexOf(concepts[i]);
            if(index != -1){
              opt.attr("selected", "");
            }
          }
          select.append(opt);
          container.append(select);
        }         
        $(".chosen-select").chosen();
        }})

        this.annotator.viewer.addField({
          load: function(field, annotation){            
            field.innerHTML = "";
            var x = $("<div>");
            x.attr("class", "annotator-tags");
            for(i in annotation.tags){
              var t = $("<span>");
              t.attr("class", "annotator-tag");
              t.append(annotation.tags[i]);
              x.append(t);
            }
            $(field).append(x);
          }
        });

        this.annotator.subscribe("annotationEditorSubmit", function(editor, annotation){
          var tags = $("div#tagContainer > div.chosen-container > ul.chosen-choices > li.search-choice > span");
          var tagArray = [];
          for (var i = 0; i < tags.length; i++) {
            var tag = $(tags[i]).html();
            tagArray[i] = tag;
          };
          annotation.tags = tagArray;
          console.log(annotation);
        })
      				
  }
  return plugin;
}	