Annotator.Plugin.SAConcepts = function (element, options) {
  var plugin = {};
  var concepts;
  plugin.pluginInit = function () {
    console.log("SAConcepts plugin init");
    var ann = this.annotator;
    console.log(ann.plugins.Store.loadAnnotations);
    var opts = {
        type: "GET",
        dataType: "json"
    };
    $.ajax(options.tagsLocation, opts).done(function(data){
      console.log("------ done ------");
      concepts = data;
    });
    console.log("------ concepts: ------");
    console.log(concepts);
    
    console.log("---- annotator ---------");
    console.log(ann);
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
        select.attr("data-placeholder","Select a tag...");
        select.attr("multiple", "");
        select.attr('tabindex','7');
        select.attr("style","width: 350px;");
        for(var i in concepts){
          var group = $("<optgroup>");
          group.attr("label", concepts[i].name);
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
        $("#tagSelector").chosen({max_selected_options: 1});
        $("#tagSelector").chosen().change(function(eve, obj){
          if(obj.deselected != undefined && obj.deselected.indexOf("Tactic") != -1){
            annotation.tactic = undefined;  
          }
        });
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
              if(annotation.tag != undefined && annotation.tag.indexOf("Tactic") != -1 && annotation.tactic == undefined){
                var tag = annotation.tag;
                var a = $("<a>");
                a.attr("href", "#");
                a.append("add Tactic");
                a.attr("data-toggle", "modal");
                a.attr("data-target", "#myModal");
                a.click(function(){
                  ann.viewer.hide();
                });
                $('#myModal').on('shown.bs.modal', function (e) {
                  console.log(e);
                  console.log(tag);
                  var title = tag.slice(6);
                  $("#tacticsTitle").html("Tactics " + title);
                  var options = {
                    type: "GET",
                    dataType: "json"
                  };
                  $.ajax("getTactics/" + tag, options).done(function(data){
                    var list = $("<ul>");
                    list.attr("class", "list-group");
                    list.attr("style", "margin: 0px; padding-right:15px;");
                    for(var i in data.tacticGroups){
                      var group = data.tacticGroups[i];
                      //{ name: xpto, tags: [...]}
                      var head = $("<li>");
                      head.attr("class", "list-group-item list-group-item-info");
                      head.append(group.name);
                      list.append(head);
                      for(var j in group.tags){
                        var tacticName = group.tags[j];
                        var tactic = $("<li>");
                        tactic.attr("class", "list-group-item");
                        tactic.append(group.tags[j]);
                        var sel = $("<a>");
                        sel.attr("href", "#");
                        sel.attr("style", "float: right;");
                        sel.append("(Select)");
                        sel.click(function(){
                          //annotation.tactic = group.tags[j];
                          var tactic = $(this).parent()[0].childNodes[0].data;
                          console.log(tactic);
                          var loc = ann.plugins.Store.options.prefix;
                          loc += "/addTactic/" + annotation.id;
                          var data = {tactic: tactic}; 
                          console.log(loc);
                          var options = {
                            type: "POST",
                            data: JSON.stringify(data),
                            contentType: "application/json; charset=utf-8",
                            dataType   : "json"
                          }
                          $('#myModal').modal('hide')
                          $.ajax(loc, options).done(function(data){
                            console.log("annotation saved");
                            ann.plugins.Store.loadAnnotations();
                          });
                        });
                        tactic.append(sel);
                        list.append(tactic);
                      }
                    }
                    $("#tacticsBody").html("")
                    $("#tacticsBody").append(list);
                  });
                  $("#saveTactic").on('click', function(e){
                    console.log("save me");
                  });
                });
                $(field).append(a);
              }else{
                var a = $("<a>");
                a.attr("href", "/fragmentManager/" + options.docId + "/" + annotation.id);
                a.attr("target", "_parent");
                a.append("fragment manager");
                $(field).append(a);
              }
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