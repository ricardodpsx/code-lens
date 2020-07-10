import Grid from "@material-ui/core/Grid/Grid";
import Paper from "@material-ui/core/Paper/Paper";
import React from "react";
import Typography from "@material-ui/core/Typography/Typography";
import Box from "@material-ui/core/Box/Box";
import {makeStyles} from "@material-ui/core";
import {connect} from "react-redux";
import SearchResults from "./SearchResults";
import DirectoryTree from "../fileViewer/DirectoryTree";
import {selectTab} from "../../appModel";
import QuerySearch from "./QuerySearch";
import Tabs from "@material-ui/core/Tabs/Tabs";
import Tab from "@material-ui/core/Tab/Tab";
import Metrics from "./charts/Metrics";
import SmellList from "./SmellList";
import FileViewer from "../fileViewer/FileViewer";
import CodeEntityData from "../CodeData";
import History from "./charts/History";
import EvolutionOfFrequency from "./charts/EvolutionOfFrequency";

const useStyles = makeStyles(theme => ({
  control: {
    padding: theme.spacing(2),
  },
}));

function TabPanel(props) {
  const {children, value, index, ...other} = props;

  return (
     <Typography
        component="div"
        role="tabpanel"
        hidden={value !== index}
        id={`simple-tabpanel-${index}`}
        aria-labelledby={`simple-tab-${index}`}
        {...other}>
       <Box p={3}>{value === index ? children : null}</Box>
     </Typography>
  );
}


function CodeExplorer({activeTab, tabItems: TabItems, selectedFile}) {
  const classes = useStyles();

  return (
     <Grid container spacing={3} className={classes.control}>
       <Grid container item spacing={3}>
         <Grid item xs={6}>
           <QuerySearch/>
         </Grid>
       </Grid>
       <Grid item xs={2}>
         <Paper>
           <SmellList/>
           <DirectoryTree/>
         </Paper>
       </Grid>
       <Grid item xs={8}>
         <Tabs value={activeTab} onChange={(event, newValue) => selectTab(newValue)}>
           <Tab label="Search results" value={TabItems.SearchResultsTab}/>
           <Tab label="Frequency" value={TabItems.FrequenciesTab}/>
           <Tab label="Evolution Metrics" value={TabItems.EvolutionOfMetric}/>
           <Tab label="Evolution of Frequency" value={TabItems.EvolutionOfFrequency}/>
           {selectedFile &&
           <Tab label={selectedFile} value={TabItems.SelectedFile}/>}

         </Tabs>
         <TabPanel value={activeTab} index={TabItems.SearchResultsTab}>
           <Paper><SearchResults/></Paper>
         </TabPanel>
         <TabPanel value={activeTab} index={TabItems.FrequenciesTab}>
           <Paper>
             <Metrics/>
           </Paper>
         </TabPanel>


         <TabPanel value={activeTab} index={TabItems.EvolutionOfMetric}>
           <Paper>
             <History/>
           </Paper>
         </TabPanel>

         <TabPanel value={activeTab} index={TabItems.EvolutionOfFrequency}>
           <Paper>
             <EvolutionOfFrequency/>
           </Paper>
         </TabPanel>


         <TabPanel value={activeTab} index={TabItems.SelectedFile}>
           <Paper>
             <CodeEntityData/>

             {selectedFile &&
             <FileViewer/>
             }
           </Paper>
         </TabPanel>

       </Grid>
     </Grid>
  );
}

function mapStateToProps({tabs: {activeTab, items: tabItems}, selectedFile: {fileName: selectedFile}}) {
  return {activeTab, tabItems, selectedFile}
}

export default connect(mapStateToProps)(CodeExplorer)