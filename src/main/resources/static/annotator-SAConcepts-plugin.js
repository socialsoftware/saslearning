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
      load: function(field, annotation){
        loadTagSelector(field, annotation, concepts);
      }
    });

    this.annotator.viewer.addField({
      load: function(field, annotation){ 
        loadTagViewer(field, annotation);
      }
    }).addField({
      load: function(field, annotation){
        loadViewerLink(field, annotation, ann, options);
      }
    });

    this.annotator.subscribe("annotationEditorShown", function(editor, annotation){
      removeExtras();
      $("#annotator-field-0").attr("disabled","true");
    }).subscribe("annotationEditorSubmit", function(editor, annotation){
      var tag = document.getElementById("tagSelector").value;
      annotation.tag = tag;
    }).subscribe("annotationsLoaded", function(a){
      console.log("annotations loaded");  
      var annotations = document.getElementsByClassName("annotator-hl");
      for(var i = 0; i < annotations.length; i++){
        var ann = annotations[i];
        var id = $(ann).attr("data-annotation-id");
        var a = document.createElement("a");
        a.href="";
        a.name = id;
        ann.appendChild(a);
        if(document.location.hash === '#'+id){
          var jann = $(ann);
          console.log("they see me scrolling...");
          $(window).scrollTop(jann.offset().top).scrollLeft(jann.offset().left);
        }
      }

    });

  }
  return plugin;
}	

var removeExtras = function(){
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
}

var loadTagSelector = function (field, annotation, concepts) {
  field.innerHTML="";
  var jqfield = $(field);
  var container = $("<div>");
  container.attr("id", "tagContainer");
  container.attr("style", "width:350px");
  jqfield.append(container);
  var select = $("<select>");
  select.attr("id", "tagSelector");
  select.attr("class", "form-control");
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
}
var loadTagViewer = function(field, annotation){           
  field.innerHTML = "";
  var x = $("<div>");
  x.attr("class", "annotator-tags");
  var t = $("<span>");
  t.attr("class", "annotator-tag");
  t.append(annotation.tag);
  x.append(t);
  $(field).append(x);
}

var loadViewerLink = function(field, annotation, ann, options){
  if(annotation.tag != undefined){
    var a = $("<a>");
    a.attr("target", "_parent");
    if(annotation.connectedId != undefined){
      a.append("View Annotation");
      a.attr("href","/viewTemplate/"+options.docId+"/"+annotation.connectedId+"/"+annotation.id);
    }else{
      a.attr("data-toggle","modal");
          a.attr("data-target","#syntax");
      a.append("Add/Remove from Structured Representation");
      var tag = encodeURIComponent(annotation.tag);
      a.click(function(){
        ann.viewer.hide();
        console.log(tag);
        console.log(annotation);
        var loc = "/addAnnotationToStructure/"+options.docId+"/"+annotation.id + "/"+tag;
        console.log(loc);
        $(parent.document.getElementById("elements")).load(loc);
    });
          
    }
    $(field).append(a);
  }
}