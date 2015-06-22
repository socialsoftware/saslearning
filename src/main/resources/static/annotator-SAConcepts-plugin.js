Annotator.Plugin.SAConcepts = function (element, options) {
  var plugin = {};
  var concepts;
  plugin.pluginInit = function () {
    var annotator = this.annotator;
    console.log("SAConcepts plugin init");
    var opts = {
        type: "GET",
        dataType: "json",
        success: function(data, textStatus, jqXHR){
          console.log("----- request succeded ------");
          console.log(data);
          console.log(textStatus);
          console.log(jqXHR);
        },
        complete: function(jqXHR, textStatus){
          console.log("------ request completed ------");
          console.log(jqXHR);
          console.log(textStatus);
        },
        error: function(jqXHR, textStatus, error){
          console.log("------ request error ------");
          console.log(jqXHR);
          console.log(textStatus);
          console.log(error);
        }
    };
    $.ajax(options.tagsLocation, opts).done(function(data){
      console.log("------ done ------");
      concepts = data;
    });
    console.log("------ concepts: ------");
    console.log(concepts);
  	this.annotator.editor.addField({
      load: function (field, annotation) {
        field.innerHTML="";
        var jqfield = $(field);
        var container = $("<div>");
        container.attr("id", "tagContainer");
        container.attr("style", "width:350px");
        jqfield.append(container);
        var select = $("<select>");
        //select.attr('class', 'chosen-select-deselect');
        select.attr("id", "tagSelector");
        select.attr("data-placeholder","Select a tag...");
        select.attr("multiple", "");
        select.attr('tabindex','7');
        select.attr("style","width: 350px;");
        for(var i in concepts){
          var group = $("<optgroup>");
          group.attr("label", i);
          for (var j = 0; j < concepts[i].length; j++) {
            var opt  = $("<option>");
            opt.append(concepts[i][j]);
            if(annotation.tag != undefined){
              var tag = annotation.tag;
              if(tag == concepts[i][j]){
                opt.attr("selected", "");
              }
            }
            group.append(opt);
          }
          select.append(group);
        }
        container.append(select);
        $("#tagSelector").chosen({max_selected_options: 1});
      }});

        this.annotator.viewer.addField({
          load: function(field, annotation){            
            field.innerHTML = "";
            var x = $("<div>");
            x.attr("class", "annotator-tags");
              var t = $("<span>");
              t.attr("class", "annotator-tag");
              t.append(annotation.tag);
              x.append(t);
            $(field).append(x);
          }
        }).addField({
          load: function(field, annotation){
            
            if(annotation.tag != undefined){
              var a = $("<a>");
              a.attr("href", "/fragmentManager/" + options.docId + "/" + annotation.id);
              a.attr("target", "_parent");
              a.append("fragment manager");
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
          var tag = $("div#tagContainer > div.chosen-container > ul.chosen-choices > li.search-choice > span").html();
          annotation.tag = tag;
        }).subscribe("annotationsLoaded", function(annotations){
          console.log("annotations loaded");
        });
      				
  }
  return plugin;
}	