
 (def dummy-content
 {:title "Events Mashup"
   :event-data [{ :event-name "event name 1"
                        :performer "performer 1"
                      :date "date 1"
                           :start-time "start time 1"
                           :end-time "end time 1"}
      {:event-name "event name 2"
                           :performer "performer 2"
                          :date "date 2"
                           :start-time "start time 2"
                           :end-time "end time 2"}]})
 
 (defn template-div []
  (enlive-html/html [:style {:type "text/css"} 
            ".Table
    {
        display: table;
    }
    .Title
    {
        display: table-caption;
        font-weight: bold;
        font-size: larger;
    }
    .Heading
    {
        display: table-row;
        font-weight: bold;
        text-align: center;
    }
    .Row
    {
        display: table-row;
    }
    .Cell
    {
        display: table-cell;
        border: solid;
        border-width: thin;
        padding-left: 5px;
        padding-right: 5px;
    }"]
           [:div {:class "Table"}
                [:div {:class "Title"}
                      [:p "This is a Table"]]
                [:div {:class "Heading"}
                      [:div {:class "Cell"}
                            [:p "Heading 1"]]]
                [:div {:class "Row"}
                      [:div {:class "Cell"}
                            [:p "Row 1 Column 1"]]]]))