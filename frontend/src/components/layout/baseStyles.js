import {makeStyles} from "@material-ui/core";

export const useStyles = makeStyles(theme => ({
  root: {
    width: '100%',
    marginTop: 10,
    marginBottom: 10
  },
  heading: {
    fontSize: theme.typography.pxToRem(15),
    fontWeight: theme.typography.fontWeightRegular,
  },
  paper: {
    padding: 8
  },
  listItem: {
    color: "#004057",
    borderBottom: "1px solid #ddd"
  }
}));
