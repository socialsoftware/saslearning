Annotator.Plugin.SAConcepts = function (element, tagsLocation) {
  var plugin = {};
  var concepts = [];
  plugin.pluginInit = function () {
    var annotator = this.annotator;
    console.log("SAConcepts plugin init");
    var opts = {
        type: "GET",
        dataType: "json",
        success: function(data){
          concepts = data.tags;
        }

    };
    var req = $.ajax(tagsLocation, opts);

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
        for (var i = 0; i < concepts.length; i++) {
          var opt  = $("<option>");
          opt.append(concepts[i]);
          /*if(annotation.tags != undefined){
            var index = annotation.tags.indexOf(concepts[i]);
            if(index != -1){
              opt.attr("selected", "");
            }
          }*/
          console.log(annotation.tag);
          if(annotation.tag != undefined){

            var tag = annotation.tag;
            if(tag == concepts[i]){
              opt.attr("selected", "");
            }
          }

          select.append(opt);
          container.append(select);
        }         
        //$("#tagSelector").chosen({allow_single_deselect: true});
        $("#tagSelector").chosen({max_selected_options: 1});
      }});

        this.annotator.viewer.addField({
          load: function(field, annotation){            
            field.innerHTML = "";
            var x = $("<div>");
            x.attr("class", "annotator-tags");
            //for(i in annotation.tags){
              var t = $("<span>");
              t.attr("class", "annotator-tag");
              t.append(annotation.tag);
              x.append(t);
            //}
            $(field).append(x);
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
          /*console.log("annotationEditorSubmit event.")
          var tags = $("div#tagContainer > div.chosen-container > ul.chosen-choices > li.search-choice > span");
          var tagArray = [];
          for (var i = 0; i < tags.length; i++) {
            var tag = $(tags[i]).html();
            tagArray[i] = tag;
          }
          annotation.tags = tagArray; */
          //var tag = $("a.chosen-single > span")[0].innerHTML;
          //console.log(tag);
          var tag = $("div#tagContainer > div.chosen-container > ul.chosen-choices > li.search-choice > span").html();
          annotation.tag = tag;
          //console.log(annotation);
        });
      				
  }
  return plugin;
}	