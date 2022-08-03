INSERT INTO users(Id, username, password, balance)
VALUES (1, 'foo', 'bar',0);

INSERT INTO items(Id, item_name, description, photo_url, start_price, purchase_price, buyer_id, seller_id, last_bid, sold)
VALUES (1, 'Chokito', 'bar of chocolate', 'https://www.coco.com',0,10, null ,1, null, 0),
       (2, 'Bounty', 'bar of chocolate', 'https://www.coco.com',0,10,null,1, null, 0),
       (3, 'Szamba', 'bar of chocolate', 'https://www.wcoco.com',0,10,null,1, null, 0),
       (4, 'Kapuciner', 'bar of chocolate', 'https://www.coco.com',0,10,null,1, null, 0),
       (5, 'Turo rudi', 'bar of chocolate', 'https://www.coco.com',0,10,null,1, null, 0),
       (6, 'Vadasz', 'bar of chocolate', 'https://www.coco.com',0,10,null,1, null, 0);