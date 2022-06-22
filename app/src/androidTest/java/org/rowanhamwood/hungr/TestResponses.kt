package org.rowanhamwood.hungr

val successfulResponse = """
    {
      "from": 1,
      "to": 20,
      "count": 10000,
      "_links": {
        "next": {
          "href": "https://api.edamam.com/api/recipes/v2?q=pie&app_key=801fbc496f6b39e5afbe4b810261585f&field=uri&field=label&field=images&field=source&field=url&_cont=CHcVQBtNNQphDmgVQntAEX4BYl1tBAMHSmJFBmIaZVFwAQYBUXlSVjMSYVFyV1VSEGcSBTRFZFJ6BAAFS2YSVTcUawZwBlAVLnlSVSBMPkd5BgMbUSYRVTdgMgksRlpSAAcRXTVGcV84SU4%3D&imageSize=LARGE&type=public&app_id=2023fe67",
          "title": "Next page"
        }
      },
      "hits": [
       
        {
          "recipe": {
            "uri": "http://www.edamam.com/ontologies/edamam.owl#recipe_76af243a27a2c3f938003d551238833e",
            "label": "Chess Pie",
            "images": {
              "THUMBNAIL": {
                "url": "thumbnail",
                "width": 100,
                "height": 100
              },
              "SMALL": {
                "url": "small",
                "width": 200,
                "height": 200
              },
              "REGULAR": {
                "url": "regular",
                "width": 300,
                "height": 300
              },
              "LARGE": {
                "url": "large",
                "width": 600,
                "height": 600
              }
            },
            "source": "Good Housekeeping",
            "url": "http://www.goodhousekeeping.com/food-recipes/a15642/chess-pie-recipe-clv0314/"
          },
          "_links": {
            "self": {
              "href": "https://api.edamam.com/api/recipes/v2/76af243a27a2c3f938003d551238833e?type=public&app_id=2023fe67&app_key=801fbc496f6b39e5afbe4b810261585f",
              "title": "Self"
            }
          }
        },
        {
          "recipe": {
            "uri": "http://www.edamam.com/ontologies/edamam.owl#recipe_985748e59463ec8cd0a4182f22ad0e42",
            "label": "Limesicle Summer Pie-Sicles",
            "images": {
              "THUMBNAIL": {
                "url": "thumbnail",
                "width": 100,
                "height": 100
              },
              "SMALL": {
                "url": "small",
                "width": 200,
                "height": 200
              },
              "REGULAR": {
                "url": "regular",
                "width": 300,
                "height": 300
              },
              "LARGE": {
                "url": "large",
                "width": 600,
                "height": 600
              }
            },
            "source": "Rachael Ray",
            "url": "http://www.rachaelray.com/recipe/limesicle-summer-pie-sicles/"
          },
          "_links": {
            "self": {
              "href": "https://api.edamam.com/api/recipes/v2/985748e59463ec8cd0a4182f22ad0e42?type=public&app_id=2023fe67&app_key=801fbc496f6b39e5afbe4b810261585f",
              "title": "Self"
            }
          }
        }
      ]
    }    
"""
.trimIndent()

val errorResponse = "I am not a json :o"