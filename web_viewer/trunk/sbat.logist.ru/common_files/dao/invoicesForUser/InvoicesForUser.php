<?php
namespace DAO;
require_once 'IInvoicesForUser.php';
include_once __DIR__.'/../DAO.php';


class InvoicesForUserEntity implements IInvoicesForUserEntity
{
    private static $invoiceStatusRusNames = array(
        'CREATED' => 'Внутренняя заявка добавлена',
        'DELETED' => 'Внутренняя заявка удалена',
        # insider request statuses
        'APPROVING' => 'Выгружена на утверждение торговому представителю',
        'RESERVED' => 'Резерв',
        'APPROVED' => 'Утверждена к сборке',
        'STOP_LIST' => 'Стоп-лист',
        'CREDIT_LIMIT' => 'Кредитный лимит',
        'RASH_CREATED' => 'Создана расходная накладная',
        'COLLECTING' => 'Выдана на сборку',
        'CHECK' => 'На контроле',
        'CHECK_PASSED' => 'Контроль пройден',
        'PACKAGING' => 'Упаковано',
        'READY' => 'Готова к отправке',
        # invoice statuses
        'DEPARTURE' => 'Убытие',
        'ARRIVED' => 'Прибытие в пункт',
        'ERROR' => 'Ошибка',
        'DELIVERED' => 'Доставлено'
    );
    private static $_instance;
    private $_DAO;

    protected function __construct()
    {
        $this->_DAO = DAO::getInstance();
        self::$_instance = $this;
    }

    public static function getInstance()
    {
        if (is_null(self::$_instance)) return new InvoicesForUserEntity();
        return self::$_instance;
    }

    /**
     * @return array
     */
    function selectAllData()
    {
        $count = 20;
        $start = 0;
        switch (func_num_args()) {
            case 2:
                $start = func_get_arg(0);
                $count = func_get_arg(1);
                break;
            case 1:
                $start = func_get_arg(0);
                break;
        }
        $array = $this->_DAO->select(new EntitySelectAllInvoicesForUser($start,$count));
        for($i=0;$i<count($array);$i++) {
            $array[$i]['invoiceStatusID'] = self::$invoiceStatusRusNames[$array[$i]['invoiceStatusID']];
        }
        return $array;
    }

    /**
     * @param $keyword
     * @return array
     */
}

class EntitySelectAllInvoicesForUser implements IEntitySelect
{
    private $start, $count,$orderByColumn, $isDesc,$searchString;
    function __construct($start, $count) {
        $this->start = DAO::getInstance()->checkString($start);
        $this->count = DAO::getInstance()->checkString($count);
        $this->isDesc = ($_POST['order'][0]['dir'] === 'desc' ? 'TRUE' : 'FALSE');
        $this->searchString='';
        $searchArray = $_POST['columns'];
        for ($i=0;$i<count($searchArray);$i++) {
            if ($searchArray[$i]['search']['value']!=='') {
                $this->searchString.=$searchArray[$i]['name'].','.$searchArray[$i]['search']['value'].';';
            }
        }
        $this->orderByColumn = $searchArray[$_POST['order'][0]['column']]['name'];
    }
    function getSelectQuery()
    {
        $userID = \PrivilegedUser::getInstance()->getUserInfo()->getData('userID');
        return "CALL selectData($userID,$this->start,$this->count,'$this->orderByColumn',$this->isDesc,'$this->searchString');";
    }
}