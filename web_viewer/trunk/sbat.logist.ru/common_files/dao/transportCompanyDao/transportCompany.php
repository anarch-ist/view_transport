<?php
namespace DAO;
include_once __DIR__ . '/ITransportCompany.php';
include_once __DIR__ . '/../DAO.php';

class TransportCompany implements ITransportCompany
{
    private static $_instance;
    private $_DAO;

    public function __construct()
    {
        $this->_DAO = DAO::getInstance();
        self::$_instance = $this;
    }

    public static function getInstance()
    {
        if (is_null(self::$_instance)) return new TransportCompany();
        return self::$_instance;
    }

    function selectAllCompanies()
    {
        $array = $this->_DAO->select(new SelectAllCompanies());
        return $array;
    }

    function selectCompanyById($id)
    {
        $array = $this->_DAO->select(new SelectCompanyById($id));
        return new TransportCompanyData($array[0]);
    }

    function selectByRange($start = 0, $count = 20)
    {
        $array = $this->_DAO->multiSelect(new SelectTransportCompaniesByRange($start, $count));
        $arrayResult = array();
        $arrayResult['transportCompanies'] = $array[0];
        $arrayResult['totalFiltered'] = $array[1][0]['totalFiltered'];
        $arrayResult['totalCount'] = $array[2][0]['totalCount'];
        return $arrayResult;
    }


    function insertCompany($transportCompanyInfo)
    {
        return $this->_DAO->insert(new InsertCompany($transportCompanyInfo));
    }

    function selectLastInsertedId()
    {
        $array = $this->_DAO->select(new SelectLastInsertedCompanyID());
        return new TransportCompanyData($array[0]);
    }

    function removeCompany($userID)
    {
        return $this->_DAO->delete(new RemoveCompany($userID));
    }

    function updateCompany(TransportCompanyData $newCompany, $id)
    {
        return $this->_DAO->update(new UpdateCompany($newCompany, $id));
    }
}

class SelectAllCompanies implements IEntitySelect {
    function getSelectQuery()
    {
        return "SELECT * FROM `transport_companies`";
    }

    public function __construct()
    {
    }

}

class SelectCompanyById implements IEntitySelect{
    var $id;
    function getSelectQuery()
    {
        return "SELECT * FROM `transport_companies` WHERE id = $this->id ";
    }

    public function __construct($id)
    {
        $this->id = DAO::getInstance()->checkString($id);
    }

}

class InsertCompany implements IEntityInsert{
    private $name, $short_name, $inn, $KPP, $BIK, $cor_account, $cur_account, $bank_name, $legal_address,
        $post_address, $keywords, $director_fullname, $chief_acc_fullname;

    public function __construct($companyData)
    {
        $dao = DAO::getInstance();
        $this->name = $dao->checkString($companyData['name']);
        $this->short_name = $dao->checkString($companyData['short_name']);
        $this->inn = $dao->checkString($companyData['inn']);
        $this->KPP = $dao->checkString($companyData['KPP']);
        $this->BIK = $dao->checkString($companyData['BIK']);
        $this->cor_account = $dao->checkString($companyData['cor_account']);
        $this->cur_account = $dao->checkString($companyData['cur_account']);
        $this->bank_name = $dao->checkString($companyData['bank_name']);
        $this->legal_address = $dao->checkString($companyData['legal_address']);
        $this->post_address = $dao->checkString($companyData['post_address']);
        $this->keywords = $dao->checkString($companyData['keywords']);
        $this->director_fullname = $dao->checkString($companyData['director_fullname']);
        $this->chief_acc_fullname = $dao->checkString($companyData['chief_acc_fullname']);
    }

    function getInsertQuery()
    {
        return "INSERT INTO `transport_companies` (name, short_name, inn, KPP, BIK, cor_account, cur_account, bank_name, legal_address, post_address, keywords, director_fullname, chief_acc_fullname) VALUE " .
            "('$this->name', '$this->short_name', '$this->inn', '$this->KPP', '$this->BIK', '$this->cor_account', '$this->cur_account', '$this->bank_name', '$this->legal_address', '$this->post_address', '$this->keywords', '$this->director_fullname', '$this->chief_acc_fullname');";
    }
}

class SelectTransportCompaniesByRange implements IEntitySelect {
    private $start, $count, $orderByColumn, $isDesc, $searchString;

    function __construct($start, $count)
    {
        $this->start = DAO::getInstance()->checkString($start);
        $this->count = DAO::getInstance()->checkString($count);
        $this->isDesc = ($_POST['order'][0]['dir'] === 'desc' ? 'TRUE' : 'FALSE');
        $this->searchString = $_POST['search']['value'];
        $searchArray = $_POST['columns'];
        $this->orderByColumn = $searchArray[$_POST['order'][0]['column']]['name'];
    }

    /**
     * this function contains query text
     * @return string
     */
    function getSelectQuery()
    {
        return "CALL selectTransportCompanies($this->start,$this->count,'$this->orderByColumn',$this->isDesc,'$this->searchString');";
    }
}

class SelectLastInsertedCompanyId implements IEntitySelect {
    /**
     * this function contains query text
     * @return string
     */
    function getSelectQuery()
    {
        return 'SELECT * FROM `transport_companies` WHERE id = LAST_INSERT_ID()';
    }
}

class RemoveCompany implements IEntityDelete {
    private $id;

    public function __construct($id)
    {
        $this->id = DAO::getInstance()->checkString($id);
    }

    /**
     * @return string
     */
    function getDeleteQuery()
    {
        return "DELETE FROM `transport_companies` WHERE id = $this->id";
    }
}

class UpdateCompany implements IEntityUpdate
{
    private $id, $name, $short_name, $inn, $KPP, $BIK, $cor_account, $cur_account, $bank_name, $legal_address,
        $keywords, $director_fullname, $chief_acc_fullname;

    function __construct(TransportCompanyData $companyData, $id)
    {
        $dao = DAO::getInstance();
        $this->id = $dao->checkString($id);
        $this->name = $dao->checkString($companyData->getData('name'));
        $this->short_name = $dao->checkString($companyData->getData('short_name'));
        $this->inn = $dao->checkString($companyData->getData('inn'));
        $this->KPP = $dao->checkString($companyData->getData('KPP'));
        $this->BIK = $dao->checkString($companyData->getData('BIK'));
        $this->cor_account = $dao->checkString($companyData->getData('cor_account'));
        $this->cur_account = $dao->checkString($companyData->getData('cur_account'));
        $this->bank_name = $dao->checkString($companyData->getData('bank_name'));
        $this->legal_address = $dao->checkString($companyData->getData('legal_address'));
        $this->keywords = $dao->checkString($companyData->getData('keywords'));
        $this->director_fullname = $dao->checkString($companyData->getData('director_fullname'));
        $this->chief_acc_fullname = $dao->checkString($companyData->getData('chief_acc_fullname'));
    }

    /**
     * @return string
     */
    function getUpdateQuery()
    {
        $query = "UPDATE `transport_companies` SET " .
            "name = '$this->name', " .
            "short_name = '$this->short_name', " .
            "inn = '$this->inn', " .
            "KPP = '$this->KPP', " .
            "BIK = '$this->BIK', " .
            "cor_account = '$this->cor_account', " .
            "cur_account = '$this->cur_account', " .
            "bank_name = '$this->bank_name', " .
            "legal_address = '$this->legal_address', " .
            "keywords = '$this->keywords', " .
            "director_fullname = '$this->director_fullname', " .
            "chief_acc_fullname = '$this->chief_acc_fullname'";
        $query = $query . " WHERE id = $this->id;";
        return $query;
    }
}