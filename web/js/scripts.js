/**
 * Funciton to load account details upon selection
 * 
 */
function submitOnSelect(){
    var frmAccountList = document.getElementById("frmAccountList");
    var cboAccountList = document.getElementById("cboAccountList");

    if(cboAccountList.value !==""){ 
        frmAccountList.submit();
    }
}

function deposit(){
    var frmAccountList = document.getElementById("frmAccountList");
    var amountValue = document.getElementById("txtAmount").value;

    if(isNumeric(amountValue)){
        document.frmAccountList.hdnActionType.value="deposit";

        frmAccountList.submit();
    }else{
        alert("You must enter a valid amount. Use numbers only.");
    }
}

function withdraw(){
    var frmAccountList = document.getElementById("frmAccountList");
    var amountValue = document.getElementById("txtAmount").value;

    if(isNumeric(amountValue)){
        document.frmAccountList.hdnActionType.value="withdraw";

        frmAccountList.submit();
    }else{
        alert("You must enter a valid amount. Use numbers only.");
    }
}

/**
* Determines if a number is numeric or not.
* this function I did not write. I used it from:
* http://stackoverflow.com/questions/9716468/is-there-any-function-like-isnumeric-in-javascript-to-validate-numbers
* @param {type} n
* @returns {Boolean}
*/
function isNumeric(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
}
