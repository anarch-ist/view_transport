# list with only manager Users
CREATE VIEW manager_users (managerUserID) AS
  SELECT users.userID
  FROM users
  WHERE users.userRoleID IN (SELECT user_roles.userRoleID
                             FROM user_roles
                             WHERE user_roles.userRoleName = 'MANAGER');


CREATE FUNCTION getInvoiceStatusIDByName(statusName VARCHAR(255))
  RETURNS INTEGER
  BEGIN
    DECLARE result INTEGER;
    SET result = (SELECT invoiceStatusID FROM invoice_statuses WHERE invoiceStatusName=statusName);
    RETURN result;
  END;
